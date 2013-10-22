package com.chughes.dip;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.chughes.data.GameRepository;
import com.chughes.dip.GameEntity.Stage;
import com.chughes.security.UserDAO;
import com.chughes.security.UserDetailsImpl;
import com.chughes.security.UserEntity;

import dip.gui.map.DefaultMapRenderer2;
import dip.gui.map.RenderCommandFactory.RenderCommand;
import dip.gui.map.SymbolInjector;
import dip.gui.order.GUIOrder;
import dip.gui.order.GUIOrder.MapInfo;
import dip.gui.order.GUIOrderFactory;
import dip.order.Order;
import dip.order.OrderException;
import dip.order.Orderable;
import dip.order.ValidationOptions;
import dip.world.Power;
import dip.world.Unit;
import dip.world.World;
import dip.world.variant.VariantManager;
import dip.world.variant.data.MapGraphic;
import dip.world.variant.data.Variant;

@Controller
public class HomeController {

	@Autowired UserDAO us;
	@Autowired private GameRepository gameRepo;
	@Autowired private GameMaster gm;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

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

			System.out.println(user.getUsername()+" is Logged In");
			loggedin = true;
		}
		//response.setContentType("application/xhtml+xml");


		GameEntity game = gameRepo.findById(id);

		World w = game.getW();

		World.VariantInfo vi = w.getVariantInfo();
		Variant variant = VariantManager.getVariant( vi.getVariantName(), vi.getVariantVersion() );
		MapGraphic mg = variant.getMapGrapic( vi.getMapName() );

		if(mg == null)
		{
			// try a default map graphic
			mg = variant.getDefaultMapGraphic();

		}

		SymbolInjector si = new SymbolInjector(variant, mg, VariantManager.getSymbolPacks()[1]);

		si.inject();

		TransformerFactory tFactory = TransformerFactory.newInstance();

		Transformer transformer = tFactory.newTransformer();

		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		transformer.transform(new DOMSource(si.getDocument()),
				result);
		sw.flush();
		sw.close();

		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		SVGDocument doc = f.createSVGDocument(VariantManager.getVariantPackageJarURL(variant).toString(), new StringReader(sw.toString()));

		DefaultMapRenderer2 mr = new DefaultMapRenderer2(doc, w, VariantManager.getSymbolPacks()[1]);

		gameRepo.setMr(mr);

		//Testing Order View
		//		Power p = w.getMap().getPowerMatching("England");
		//		Order o = new GUIOrderFactory().createHold(p, new Location(w.getMap().getProvinceMatching("lon"),Coast.NONE), Unit.Type.UNDEFINED);
		//		ArrayList<Order> al = new ArrayList<Order>();
		//		al.add(o);
		//		TurnState ts = w.getLastTurnState();
		//		ValidationOptions vo = new ValidationOptions();
		//		vo.setOption(ValidationOptions.KEY_GLOBAL_PARSING, ValidationOptions.VALUE_GLOBAL_PARSING_STRICT);
		//		o.validate(ts, vo, null);
		//		ts.setOrders(p, al);
		//		w.setTurnState(ts);
		//		mr.orderCreated((GUIHold)o);
		//End of Order Test

		RenderCommand rc = mr.getRenderCommandFactory().createRCSetTurnstate(mr, w.getLastTurnState());

		RenderCommand rc3 = mr.getRenderCommandFactory().createRCRenderAll(mr);
		//RenderCommand rc4 = mr.getRenderCommandFactory().createRCSetDisplayUnits(mr, true);
		//RenderCommand rc5 = mr.getRenderCommandFactory().createRCSetLabel(mr, MapRenderer2.VALUE_LABELS_BRIEF);
		boolean member = false;
		if (loggedin){
			UserGameEntity uge = gameRepo.inGameUser(id, user.getId());
			if (uge != null){
				member = true;
				model.addAttribute("me_id", uge.getId());
				model.addAttribute("isready", uge.isReady());
				model.addAttribute("phasetype", w.getLastTurnState().getPhase().getPhaseType().getBriefName());
				if (game.getStage() == Stage.PLAYING){
					Power p1 = w.getMap().getPowerMatching(uge.getPower());
					RenderCommand rc2 = mr.getRenderCommandFactory().createRCSetPowerOrdersDisplayed(mr, new Power[]{p1});
					mr.execRenderCommand(rc2);
					List<GUIOrder> orders = w.getLastTurnState().getOrders(p1);
					for (GUIOrder o : orders) {
						o.updateDOM(mr.new DMRMapInfo(w.getLastTurnState()));
						System.out.println(o.toFullString());
					}
				}
			}
		}
		mr.execRenderCommand(rc);

		//mr.execRenderCommand(rc4);
		//mr.execRenderCommand(rc5);
		mr.execRenderCommand(rc3);
		//ServletOutputStream os = response.getOutputStream();

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer1 = tf.newTransformer();
		transformer1.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer1.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer1.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer1.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		//ByteArrayOutputStream baos = new ByteArrayOutputStream();

		StringWriter sw1 = new StringWriter();

		transformer1.transform(new DOMSource(doc), new StreamResult(sw1));

		//session.setAttribute("svg", doc);
		
		model.addAttribute("svg", sw1.toString());
		model.addAttribute("gid", id);
		
//		model.addAttribute("position",game.getW().getLastTurnState().getPosition().getOwnedSupplyCenters());
		
		model.addAttribute("players", game.getPlayers());
		model.addAttribute("member_of_game", member);
		model.addAttribute("gamephase", game.getW().getLastTurnState().getPhase().toString());
		model.addAttribute("started", game.getStage() == Stage.PLAYING);
		//session.setAttribute("game", w);

		return "board";
	}

	@RequestMapping(value = "/game/{gameID}/JSONorder")
	public @ResponseBody Map<String, ?> move(@PathVariable(value="gameID") int id,@RequestBody UIOrder order) throws Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails user1 = (UserDetails)auth.getPrincipal();
		UserDetailsImpl user = (UserDetailsImpl) user1;

		UserEntity ue = us.getUserEntity(user.getId());

		GameEntity game = gameRepo.findById(id);

		World w = game.getW();

		UserGameEntity uge = gameRepo.inGameUser(id, ue.getId());

		Power p = w.getMap().getPowerMatching(uge.getPower());

		DefaultMapRenderer2 mr = gameRepo.getMr();

		Order o = null;
		switch (order.getType()) {
		case "order-move":
			o = new GUIOrderFactory().createMove(p, mr.getLocation(order.getLoc()), Unit.Type.UNDEFINED, mr.getLocation(order.getLoc1()));
			break;
		case "order-hold":
			o = new GUIOrderFactory().createHold(p, mr.getLocation(order.getLoc()), Unit.Type.UNDEFINED);
			break;
		case "order-shold":
			o = new GUIOrderFactory().createSupport(p, mr.getLocation(order.getLoc()),Unit.Type.UNDEFINED, mr.getLocation(order.getLoc1()), w.getLastTurnState().getPosition().getUnit(w.getMap().getProvinceMatching(order.getLoc1())).getPower(), Unit.Type.UNDEFINED);
			break;
		case "order-smove":
			o = new GUIOrderFactory().createSupport(p, mr.getLocation(order.getLoc()),Unit.Type.UNDEFINED, mr.getLocation(order.getLoc1()), w.getLastTurnState().getPosition().getUnit(w.getMap().getProvinceMatching(order.getLoc1())).getPower(), Unit.Type.UNDEFINED,mr.getLocation(order.getLoc2()));
			break;
		case "order-convoy":
			o = new GUIOrderFactory().createConvoy(p, mr.getLocation(order.getLoc()), Unit.Type.UNDEFINED, mr.getLocation(order.getLoc1()), w.getLastTurnState().getPosition().getUnit(w.getMap().getProvinceMatching(order.getLoc1())).getPower(), Unit.Type.UNDEFINED,mr.getLocation(order.getLoc2()));
			break;
		case "order-retreat":
			o = new GUIOrderFactory().createRetreat(p, mr.getLocation(order.getLoc()), Unit.Type.UNDEFINED, mr.getLocation(order.getLoc1()));
			break;
		case "order-disband":
			o = new GUIOrderFactory().createDisband(p, mr.getLocation(order.getLoc()), Unit.Type.UNDEFINED);
			break;
		case "order-builda":
			o = new GUIOrderFactory().createBuild(p, mr.getLocation(order.getLoc()), Unit.Type.ARMY);
			break;
		case "order-buildf":
			o = new GUIOrderFactory().createBuild(p, mr.getLocation(order.getLoc()), Unit.Type.FLEET);
			break;
		case "order-destroy":
			o = new GUIOrderFactory().createRemove(p, mr.getLocation(order.getLoc()), Unit.Type.UNDEFINED);
			break;
		default:
			break;
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
		return Collections.singletonMap("orders",Collections.singletonMap(id1,sw1.toString()));
	}
	
	@RequestMapping(value = "/game/{gameID}/JSONready")
	public @ResponseBody Map<String, ?> setReady(@PathVariable(value="gameID") int id){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails user1 = (UserDetails)auth.getPrincipal();
		UserDetailsImpl user = (UserDetailsImpl) user1;
		UserGameEntity uge = gameRepo.inGameUser(id, user.getId());
		boolean ready = uge.isReady();
		uge.setReady(!ready);
		gameRepo.saveInGameUser(uge);
		gm.processGame(uge.getGame());
		return Collections.singletonMap("ready", !ready);
	}
}
