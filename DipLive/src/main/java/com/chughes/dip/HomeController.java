package com.chughes.dip;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGElement;
import org.xml.sax.SAXException;

import com.chughes.data.GameRepository;
import com.chughes.data.MapHolder;
import com.chughes.dip.GameEntity.Stage;
import com.chughes.security.UserDAO;
import com.chughes.security.UserDetailsImpl;
import com.chughes.security.UserEntity;

import dip.gui.map.DefaultMapRenderer2;
import dip.gui.map.MapException;
import dip.gui.map.RenderCommandFactory.RenderCommand;
import dip.gui.map.SymbolInjector;
import dip.gui.order.GUIOrder;
import dip.gui.order.GUIOrder.MapInfo;
import dip.gui.order.GUIOrderFactory;
import dip.order.Order;
import dip.order.OrderException;
import dip.order.Orderable;
import dip.order.ValidationOptions;
import dip.world.Phase;
import dip.world.Power;
import dip.world.Province;
import dip.world.TurnState;
import dip.world.Unit;
import dip.world.World;
import dip.world.variant.VariantManager;
import dip.world.variant.data.MapGraphic;
import dip.world.variant.data.Variant;

@Controller
public class HomeController {

	@Autowired private UserDAO us;
	@Autowired private GameRepository gameRepo;
	@Autowired private MapHolder mh;
	@Autowired private GameMaster gm;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping("/help")
	public String help() {
		return "help";
	}

	@RequestMapping(value = "/")
	public String dash(Model model){
		UserDetailsImpl user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetailsImpl){
			UserDetails user1 = (UserDetails)auth.getPrincipal();
			user = (UserDetailsImpl) user1;

			UserEntity ue = us.getUserEntity(user.getId());
			model.addAttribute("user",ue);

			model.addAttribute("loggedin", true);
			model.addAttribute("games", ue.getGames());
		}
		return "dash";
	}

	@RequestMapping(value = "/game/{gameID}")
	public String home(Model model,@PathVariable(value="gameID") int id) throws Exception {
		boolean loggedin = false;
		UserDetailsImpl user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails){
			UserDetails user1 = (UserDetails)auth.getPrincipal();
			user = (UserDetailsImpl) user1;

			UserEntity ue = us.getUserEntity(user.getId());
			model.addAttribute("user",ue);
			model.addAttribute("loggedin", true);

			loggedin = true;
		}

		GameEntity game = gameRepo.findById(id);

		World w = game.getW();

		boolean member = false;
		Power p1 = null;
		if (loggedin){
			model.addAttribute("private", game.getSecret() != null);
			UserGameEntity uge = gameRepo.inGameUser(id, user.getId());
			if (uge != null){
				member = true;
				model.addAttribute("me_id", uge.getId());
				model.addAttribute("me", uge);

				model.addAttribute("isready", uge.isReady());
				model.addAttribute("phasetype", w.getLastTurnState().getPhase().getPhaseType().getBriefName());
				if (game.getStage() == Stage.PLAYING){
					Map<String,String> provinces = new HashMap<String,String>();
					for (Province p:game.getW().getMap().getProvinces()){
						for (String shortn :p.getShortNames()){
							provinces.put(shortn, p.getFullName());
						}
					}
					model.addAttribute("provinces", provinces);
					//Show orders just for the logged in user.
					p1 = w.getMap().getPowerMatching(uge.getPower());
					List<GUIOrder> orders = w.getLastTurnState().getOrders(p1);
					Map<String,String> textorders = new HashMap<String,String>();
					for (GUIOrder o : orders) {
						System.out.println(o.toFullString());
						textorders.put(o.getSource().toString(), o.toFullString());
					}
					model.addAttribute("textorders", textorders);
				}
			}
		}

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer1 = tf.newTransformer();
		transformer1.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer1.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer1.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer1.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		//ByteArrayOutputStream baos = new ByteArrayOutputStream();

		StringWriter sw1 = new StringWriter();

		transformer1.transform(new DOMSource(renderSVG(id,w, p1, null)), new StreamResult(sw1));

		model.addAttribute("svg", sw1.toString());
		model.addAttribute("gid", id);
		DefaultMapRenderer2 mr = mh.getMr(id);
		Set<UserGameEntity> players = game.getPlayers();
		if (game.getStage() == Stage.PLAYING){
			for (UserGameEntity player : players) {
				player.setColor(mr.getMapMetadata().getPowerColor(w.getMap().getPowerMatching(player.getPower())));
			}
		}
		model.addAttribute("players", players);
		model.addAttribute("member_of_game", member);
		model.addAttribute("gamephase", game.getW().getLastTurnState().getPhase().toString());
		model.addAttribute("started", game.getStage() == Stage.PLAYING);
		model.addAttribute("next", game.getTurnend());

		return "board";
	}

	private SVGDocument renderSVG(int id, World w, Power p1, String phase) throws TransformerException, IOException, SAXException, ParserConfigurationException, MapException, InterruptedException {
		World.VariantInfo vi = w.getVariantInfo();
		Variant variant = VariantManager.getVariant( vi.getVariantName(), vi.getVariantVersion() );
		MapGraphic mg = variant.getMapGrapic( vi.getMapName() );

		if(mg == null)
		{
			// try a default map graphic
			mg = variant.getDefaultMapGraphic();

		}

		SymbolInjector si = new SymbolInjector(variant, mg, VariantManager.getSymbolPacks()[2]);

		si.inject();

		TransformerFactory tFactory = TransformerFactory.newInstance();

		Transformer transformer = tFactory.newTransformer();

		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		transformer.transform(new DOMSource(si.getDocument()),
				result);
		sw.flush();
		sw.close();
		//Create an SVG Document and start drawing the map
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		SVGDocument doc = f.createSVGDocument(VariantManager.getVariantPackageJarURL(variant).toString(), new StringReader(sw.toString()));

		DefaultMapRenderer2 mr = new DefaultMapRenderer2(doc, w, VariantManager.getSymbolPacks()[3]);

		mh.setMr(id,mr);

		TurnState tState;

		if (phase != null) {
			tState = w.getTurnState(Phase.parse(phase));
		}else {
			tState = w.getLastTurnState();
			mh.setPhase(id,tState.getPhase());
		}

		RenderCommand rc = mr.getRenderCommandFactory().createRCSetTurnstate(mr, tState);

		RenderCommand rc3 = mr.getRenderCommandFactory().createRCRenderAll(mr);

		mr.execRenderCommand(rc);

		if (p1 != null) {
			//Show orders just for the logged in user.
			RenderCommand rc2 = mr.getRenderCommandFactory().createRCSetPowerOrdersDisplayed(mr, new Power[]{p1});
			mr.execRenderCommand(rc2);
			List<GUIOrder> orders = tState.getOrders(p1);
			for (GUIOrder o : orders) {
				o.updateDOM(mr.new DMRMapInfo(tState));
			}
		}else if (tState != w.getLastTurnState()) {
			RenderCommand rc2 = mr.getRenderCommandFactory().createRCSetPowerOrdersDisplayed(mr, w.getMap().getPowers());
			mr.execRenderCommand(rc2);
			List<GUIOrder> orders = tState.getAllOrders();
			for (GUIOrder o : orders) {
				o.updateDOM(mr.new DMRMapInfo(tState));
			}
		}

		//mr.execRenderCommand(rc4);
		//mr.execRenderCommand(rc5);
		mr.execRenderCommand(rc3);
		//Convert SVG Document to String
		return doc;

	}

	@RequestMapping(value="/game/phase/{gameID}/{phase}")
	public @ResponseBody List<String> phasechange(@PathVariable(value="gameID") int id,@PathVariable(value="phase") String phase){

		GameEntity game = gameRepo.findById(id);

		World w = game.getW();

		String longname = null;
		TurnState tState = w.getTurnState(mh.getPhase(id));
		if (phase.equals("previous")){
			tState = w.getPreviousTurnState(tState);
			if (tState == null){
				phase = "empty";
			}else {
				mh.setPhase(id,tState.getPhase());
				phase = tState.getPhase().getBriefName();
				longname = tState.getPhase().toString();
			}
		}else if (phase.equals("next")){
			tState = w.getNextTurnState(tState);
			if (tState == null){
				phase = "empty";
			}else {
				mh.setPhase(id,tState.getPhase());
				phase = tState.getPhase().getBriefName();
				longname = tState.getPhase().toString();
				if (tState == w.getLastTurnState()){
					phase = "current";
				}
			}
		}

		ArrayList<String> result = new ArrayList<String>(2);
		result.add(0, phase);
		result.add(1,longname);
		return result;
	}


	@RequestMapping(value="/gameimage/{gameID}/{phase}")
	public void rasterimage(@PathVariable(value="gameID") int id,@PathVariable(value="phase") String phase, HttpServletResponse response) throws Exception, IOException, SAXException, ParserConfigurationException, MapException, InterruptedException, TranscoderException{

		response.setContentType("image/jpeg");

		GameEntity game = gameRepo.findById(id);

		World w = game.getW();

		if (w.getLastTurnState().getPhase().compareTo(Phase.parse(phase)) == 0)throw new IllegalAccessError("Cannot Acccess Latest Turn");

		JPEGTranscoder t = new JPEGTranscoder();
		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, .95f);

		SVGDocument svg = renderSVG(id,w, null, phase);


		TranscoderInput input = new TranscoderInput(svg);
		TranscoderOutput tOutput = new TranscoderOutput(response.getOutputStream());

		t.transcode(input, tOutput);

		response.getOutputStream().flush();
		response.getOutputStream().close();

	}


	@PreAuthorize("hasRole('PLAYER')")
	@RequestMapping(value = "/game/{gameID}/JSONorder")
	public @ResponseBody Map<String, ?> move(@PathVariable(value="gameID") int id,@RequestBody UIOrder order) throws Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();

		UserEntity ue = us.getUserEntity(user.getId());

		GameEntity game = gameRepo.findById(id);

		World w = game.getW();

		UserGameEntity uge = gameRepo.inGameUser(id, ue.getId());

		Power p = w.getMap().getPowerMatching(uge.getPower());

		DefaultMapRenderer2 mr = mh.getMr(id);

		Order o = null;

		if (order.getType().equals("order-move")){
			o = new GUIOrderFactory().createMove(p, mr.getLocation(order.getLoc()), Unit.Type.UNDEFINED, mr.getLocation(order.getLoc1()));
		}else if (order.getType().equals("order-hold")){
			o = new GUIOrderFactory().createHold(p, mr.getLocation(order.getLoc()), Unit.Type.UNDEFINED);
		}else if (order.getType().equals("order-shold")){
			o = new GUIOrderFactory().createSupport(p, mr.getLocation(order.getLoc()),Unit.Type.UNDEFINED, mr.getLocation(order.getLoc1()), w.getLastTurnState().getPosition().getUnit(w.getMap().getProvinceMatching(order.getLoc1())).getPower(), Unit.Type.UNDEFINED);
		}else if (order.getType().equals("order-smove")){
			o = new GUIOrderFactory().createSupport(p, mr.getLocation(order.getLoc()),Unit.Type.UNDEFINED, mr.getLocation(order.getLoc1()), w.getLastTurnState().getPosition().getUnit(w.getMap().getProvinceMatching(order.getLoc1())).getPower(), Unit.Type.UNDEFINED,mr.getLocation(order.getLoc2()));
		}else if (order.getType().equals("order-convoy")){
			o = new GUIOrderFactory().createConvoy(p, mr.getLocation(order.getLoc()), Unit.Type.UNDEFINED, mr.getLocation(order.getLoc1()), w.getLastTurnState().getPosition().getUnit(w.getMap().getProvinceMatching(order.getLoc1())).getPower(), Unit.Type.UNDEFINED,mr.getLocation(order.getLoc2()));
		}else if (order.getType().equals("order-retreat")){
			o = new GUIOrderFactory().createRetreat(p, mr.getLocation(order.getLoc()), Unit.Type.UNDEFINED, mr.getLocation(order.getLoc1()));
		}else if (order.getType().equals("order-disband")){
			o = new GUIOrderFactory().createDisband(p, mr.getLocation(order.getLoc()), Unit.Type.UNDEFINED);
		}else if (order.getType().equals("order-builda")){
			o = new GUIOrderFactory().createBuild(p, mr.getLocation(order.getLoc()), Unit.Type.ARMY);
		}else if (order.getType().equals("order-buildf")){
			o = new GUIOrderFactory().createBuild(p, mr.getLocation(order.getLoc()), Unit.Type.FLEET);
		}else if (order.getType().equals("order-destroy")){
			o = new GUIOrderFactory().createRemove(p, mr.getLocation(order.getLoc()), Unit.Type.UNDEFINED);
		}

		ValidationOptions vo = new ValidationOptions();
		vo.setOption(ValidationOptions.KEY_GLOBAL_PARSING, ValidationOptions.VALUE_GLOBAL_PARSING_STRICT);
		try{
			o.validate(w.getLastTurnState(), vo, w.getRuleOptions());
		}catch(OrderException oe){
			return Collections.singletonMap("error", new String[]{oe.getLocalizedMessage()});
		}
		logger.info("From: "+o.getSourceUnitType());

		MapInfo info = mr.new DMRMapInfo(w.getLastTurnState());
		SVGElement element = ((GUIOrder)o).orderSVG(info);


		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer1 = tf.newTransformer();
		transformer1.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer1.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer1.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer1.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		//ByteArrayOutputStream baos = new ByteArrayOutputStream();

		StringWriter sw1 = new StringWriter();


		transformer1.transform(new DOMSource(element), new StreamResult(sw1));

		//Preventing two orders for same unit
		List<Orderable> orders = w.getLastTurnState().getOrders(p);
		Iterator<Orderable> iter = orders.iterator();
		//boolean isDuplicate = false;
		while(iter.hasNext())
		{
			//System.out.println("Looping");
			Orderable listOrder = iter.next();
			if( listOrder.getSource().isProvinceEqual(o.getSource()) )
			{
				//System.out.println("Should be removed");
				iter.remove();
			}
		}
		orders.add(o);

		//model.addAttribute("success", 1);

		gameRepo.updateWorld(w);
		String id1 = info.getPowerSVGGElement(p, 1).getId();
		Map<String,Object> results = new HashMap<String,Object>();
		results.put("orders",Collections.singletonMap(id1,sw1.toString()));
		results.put("orders_text", Collections.singletonMap(o.getSource().toString(),o.toFullString()));
		return results;
	}

	@PreAuthorize("hasRole('PLAYER')")
	@RequestMapping(value = "/game/{gameID}/JSONorder-remove")
	public @ResponseBody String remove(@PathVariable(value="gameID") int id,@RequestParam(value="prov") String province) throws Exception {

		province = URLDecoder.decode( province, "UTF-8" );

		province = province.replace("/", "-");

		System.out.println("In:" + province);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
		GameEntity game = gameRepo.findById(id);
		World w = game.getW();
		UserGameEntity uge = gameRepo.inGameUser(id, user.getId());

		Power p = w.getMap().getPowerMatching(uge.getPower());

		DefaultMapRenderer2 mr = mh.getMr(id);

		List<Orderable> orders = w.getLastTurnState().getOrders(p);
		Iterator<Orderable> iter = orders.iterator();
		//boolean isDuplicate = false;
		while(iter.hasNext())
		{
			//System.out.println("Looping");
			Orderable listOrder = iter.next();
			if( listOrder.getSource().isProvinceEqual(mr.getLocation(province)) )
			{
				//System.out.println("Should be removed");
				iter.remove();
				gameRepo.updateWorld(w);
				return "success";
			}
		}

		return "fail";

	}

	@PreAuthorize("hasRole('PLAYER')")
	@RequestMapping(value = "/game/{gameID}/JSONready")
	public @ResponseBody Map<String, ?> setReady(@PathVariable(value="gameID") int id){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails user1 = (UserDetails)auth.getPrincipal();
		UserDetailsImpl user = (UserDetailsImpl) user1;
		UserGameEntity uge = gameRepo.inGameUser(id, user.getId());
		boolean ready = uge.isReady();
		if (uge.isOrderable()){
			uge.setReady(!ready);
			gameRepo.saveInGameUser(uge);
			gm.processGame(uge.getGame());
		}
		return Collections.singletonMap("ready", uge.isReady());
	}
}
