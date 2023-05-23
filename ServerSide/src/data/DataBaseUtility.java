package data;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import data.datatype.Account;
import data.datatype.Item;
import data.datatype.Item.ItemType;
import network.handlers.ModMessageHandler;
import network.messages.ModMessage;

public class DataBaseUtility {
	
	private static final String mongoURL = "";
	
	public static void amain(String[] args) {
		System.out.println("testing MDB");
//		testMDB();
//		testbff();
		loadInitialItems();
//		Database.init();
//		ModMessage m = new ModMessage();
//		m.setUser("test");
//		new ModMessageHandler(m).handle("");
//		loadTestAcct();
//		testAccountExists();
	}
	
	public static void testAccountExists() {
		Database.init();
		if (Database.accountExists("testAcct")) {
			System.out.println("yep");
		} else {
			System.out.println("nope");
		}
	}
	
	public static void testMDB() {
		MongoClient mongoClient = MongoClients.create("mongodb+srv://collebrusco:srt8Chrysler300c@cluster0.ddejb29.mongodb.net/?retryWrites=true&w=majority");
		MongoDatabase database = mongoClient.getDatabase("testDB");
		
		MongoCollection<Document> collection = database.getCollection("testCollection");
		
		Document doc = new Document("id", 0).append("name", "The Stranger").append("type", "book");
		
		collection.insertOne(doc);
		
	}
	
	public static void loadTestAcct() {
		MongoClient mongoClient = MongoClients.create(mongoURL);
		MongoDatabase db = mongoClient.getDatabase("library");

		CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
	    CodecRegistry codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
	    CodecRegistry customCodecRegistry = CodecRegistries.fromRegistries(codecRegistry, pojoCodecRegistry);
	    
	    MongoCollection<Account> collection = db.getCollection("accounts", Account.class).withCodecRegistry(customCodecRegistry);
	    Account acct = new Account();
	    acct.setUsername("alt");
	    acct.setPassword("admin");
	    
	    collection.insertOne(acct);
	}
	
	public static void loadInitialItems() {
		MongoClient mClient = MongoClients.create(mongoURL);
		MongoDatabase libraryDB = mClient.getDatabase("library");
		
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
	    CodecRegistry codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
	    CodecRegistry customCodecRegistry = CodecRegistries.fromRegistries(codecRegistry, pojoCodecRegistry);
		
		MongoCollection<Item> libCollection = libraryDB.getCollection("items", Item.class).withCodecRegistry(customCodecRegistry);
		
		Item mbird = new Item("To Kill a MockingBird", ItemType.BOOK);
		mbird.setImage(getImageBytesFromFile("tkamb", "jpg"));
		mbird.setDescription("\"To Kill a Mockingbird\" is a Pulitzer Prize-winning novel by Harper Lee. Set in the 1930s in a small town in Alabama, it tells the story of a young girl named Scout as she learns about racial injustice and moral courage through the trial of a black man accused of rape.");
		libCollection.insertOne(mbird);
		
		Item nfs = new Item(ItemType.GAME);
		nfs.setName("Need For Speed");
		nfs.setImage(getImageBytesFromFile("nfs", "jpg"));
		nfs.setDescription("\"Need for Speed: Hot Pursuit 2\" is a racing video game developed by EA Black Box and published by Electronic Arts. It allows players to race exotic cars on various tracks while being pursued by police cars, adding an element of excitement and challenge to the gameplay.");
		libCollection.insertOne(nfs);
		
		Item meditations = new Item();
		meditations.setName("Meditations");
		meditations.setDescription("\"Meditations\" is a collection of personal reflections and philosophical musings on life, death, and morality written by the Roman Emperor Marcus Aurelius.");
		meditations.setType(ItemType.BOOK);

		Item malteseFalcon = new Item();
		malteseFalcon.setName("The Maltese Falcon");
		malteseFalcon.setDescription("\"The Maltese Falcon\" is a classic noir novel that follows private investigator Sam Spade as he tries to unravel the mystery behind the titular Maltese Falcon.");
		malteseFalcon.setType(ItemType.BOOK);

		Item theStranger = new Item();
		theStranger.setImage(getImageBytesFromFile("stranger", "png"));
		theStranger.setName("The Stranger");
		theStranger.setDescription("\"The Stranger\" is a philosophical novel that explores the absurdity of human existence through the story of Meursault, a man who commits a senseless murder.");
		theStranger.setType(ItemType.BOOK);

		Item sherlockHolmes = new Item();
		sherlockHolmes.setName("The Adventures of Sherlock Holmes");
		sherlockHolmes.setDescription("\"The Adventures of Sherlock Holmes\" is a collection of short stories featuring the iconic detective Sherlock Holmes as he solves a series of complex and baffling mysteries.");
		sherlockHolmes.setType(ItemType.BOOK);

		Item bigSleep = new Item();
		bigSleep.setName("The Big Sleep");
		bigSleep.setDescription("\"The Big Sleep\" is a classic noir novel that follows private detective Philip Marlowe as he tries to unravel a complex web of blackmail and murder.");
		bigSleep.setType(ItemType.BOOK);

		Item beyondGoodAndEvil = new Item();
		beyondGoodAndEvil.setName("Beyond Good and Evil");
		beyondGoodAndEvil.setDescription("\"Beyond Good and Evil\" is a philosophical treatise that challenges traditional morality and calls for a new, more individualistic approach to life.");
		beyondGoodAndEvil.setType(ItemType.BOOK);

		Item nameOfTheRose = new Item();
		nameOfTheRose.setName("The Name of the Rose");
		nameOfTheRose.setDescription("\"The Name of the Rose\" is a historical mystery set in a medieval monastery, where a series of murders occur and a Franciscan friar, William of Baskerville, is called upon to solve the crimes.");
		nameOfTheRose.setType(ItemType.BOOK);

		Item hitchhikersGuide = new Item();
		hitchhikersGuide.setName("The Hitchhiker's Guide to the Galaxy");
		hitchhikersGuide.setDescription("\"The Hitchhiker's Guide to the Galaxy\" is a hilarious science fiction adventure that follows the misadventures of human Arthur Dent and his alien friend Ford Prefect as they travel through space and time after the Earth is destroyed to make way for an intergalactic highway.");
		hitchhikersGuide.setType(ItemType.BOOK);

		Item crimeAndPunishment = new Item();
		crimeAndPunishment.setName("Crime and Punishment");
		crimeAndPunishment.setDescription("\"Crime and Punishment\" is a classic novel that explores the psychological impact of committing a crime through the story of impoverished ex-student Raskolnikov, who murders an old pawnbroker and her sister in a fit of desperation.");
		crimeAndPunishment.setType(ItemType.BOOK);

		Item heartOfDarkness = new Item();
		heartOfDarkness.setName("Heart of Darkness");
		heartOfDarkness.setDescription("\"Heart of Darkness\" is a haunting exploration of the darkness that lies within the human heart, as seen through the eyes of Charles Marlow, a sailor who is sent on a journey up the Congo River to find the enigmatic and mysterious Kurtz.");
		heartOfDarkness.setType(ItemType.BOOK);

		meditations.setImage(getImageBytesFromFile("meditations", "jpg"));
		libCollection.insertOne(meditations);

		malteseFalcon.setImage(getImageBytesFromFile("falcon", "jpg"));
		libCollection.insertOne(malteseFalcon);

		libCollection.insertOne(theStranger);

		sherlockHolmes.setImage(getImageBytesFromFile("sherlock", "jpg"));
		libCollection.insertOne(sherlockHolmes);

		bigSleep.setImage(getImageBytesFromFile("bigSleep", "jpg"));
		libCollection.insertOne(bigSleep);

		beyondGoodAndEvil.setImage(getImageBytesFromFile("goodandevil", "jpg"));
		libCollection.insertOne(beyondGoodAndEvil);

		nameOfTheRose.setImage(getImageBytesFromFile("rose", "jpg"));
		libCollection.insertOne(nameOfTheRose);

		hitchhikersGuide.setImage(getImageBytesFromFile("guide", "jpg"));
		libCollection.insertOne(hitchhikersGuide);

		crimeAndPunishment.setImage(getImageBytesFromFile("crime", "jpg"));
		libCollection.insertOne(crimeAndPunishment);

		heartOfDarkness.setImage(getImageBytesFromFile("heart", "jpg"));
		libCollection.insertOne(heartOfDarkness);
		
		Item minecraft = new Item();
		minecraft.setType(ItemType.GAME);
		minecraft.setName("Minecraft");
		minecraft.setDescription("Minecraft is a sandbox game where players can explore, build, and create their own virtual worlds using blocks and resources.");
		minecraft.setImage(getImageBytesFromFile("minecraft", "jpg"));
		libCollection.insertOne(minecraft);

		Item tetris = new Item();
		tetris.setType(ItemType.GAME);
		tetris.setName("Tetris");
		tetris.setDescription("Tetris is a classic puzzle game where players must manipulate falling blocks to create complete lines and prevent the blocks from stacking up to the top.");
		tetris.setImage(getImageBytesFromFile("tetris", "jpg"));
		libCollection.insertOne(tetris);

		Item spaceInvaders = new Item();
		spaceInvaders.setType(ItemType.GAME);
		spaceInvaders.setName("Space Invaders");
		spaceInvaders.setDescription("Space Invaders is a classic arcade game where players control a spaceship and must shoot down waves of descending aliens while avoiding their attacks.");
		spaceInvaders.setImage(getImageBytesFromFile("space-invaders", "jpg"));
		libCollection.insertOne(spaceInvaders);

		Item falloutNewVegas = new Item();
		falloutNewVegas.setType(ItemType.GAME);
		falloutNewVegas.setName("Fallout New Vegas");
		falloutNewVegas.setDescription("Fallout New Vegas is a post-apocalyptic role-playing game set in a future version of Las Vegas where players can explore a vast open world, complete quests, and make choices that affect the story and characters.");
		falloutNewVegas.setImage(getImageBytesFromFile("fallout-new-vegas", "jpg"));
		libCollection.insertOne(falloutNewVegas);

		Item mario = new Item();
		mario.setType(ItemType.GAME);
		mario.setName("Mario");
		mario.setDescription("Mario is a classic platformer game featuring the iconic character Mario, where players must navigate through levels, defeat enemies, and save Princess Peach.");
		mario.setImage(getImageBytesFromFile("mario", "jpg"));
		libCollection.insertOne(mario);

		Item darkestDungeon = new Item();
		darkestDungeon.setType(ItemType.GAME);
		darkestDungeon.setName("Darkest Dungeon");
		darkestDungeon.setDescription("Darkest Dungeon is a challenging role-playing game where players must recruit and manage a team of heroes to explore dungeons, battle monsters, and manage their stress levels and psychological well-being.");
		darkestDungeon.setImage(getImageBytesFromFile("darkest-dungeon", "jpg"));
		libCollection.insertOne(darkestDungeon);

		Item civilization = new Item();
		civilization.setType(ItemType.GAME);
		civilization.setName("Civilization");
		civilization.setDescription("Civilization is a strategy game where players take on the role of a leader of a civilization, managing resources, building cities, and engaging in diplomacy, trade, and warfare with other civilizations.");
		civilization.setImage(getImageBytesFromFile("civilization", "jpg"));
		libCollection.insertOne(civilization);

		Item portal = new Item();
		portal.setType(ItemType.GAME);
		portal.setName("Portal");
		portal.setDescription("Portal is a puzzle game where players use a device that creates portals to solve physics-based puzzles and navigate through a mysterious facility run by an artificial intelligence named GLaDOS.");
		portal.setImage(getImageBytesFromFile("portal", "jpg"));
		libCollection.insertOne(portal);

		Item zelda = new Item();
		zelda.setType(ItemType.GAME);
		zelda.setName("The Legend of Zelda: Breath of the Wild");
		zelda.setDescription("The Legend of Zelda: Breath of the Wild is an open-world action-adventure game set in a vast, beautifully-rendered version of Hyrule, where players must explore the world, solve puzzles, and battle enemies to defeat the evil Calamity Ganon and save Princess Zelda.");
		zelda.setImage(getImageBytesFromFile("zelda", "jpg"));
		libCollection.insertOne(zelda);
		
		Item gtaV = new Item();
		gtaV.setType(ItemType.GAME);
		gtaV.setName("Grand Theft Auto V");
		gtaV.setDescription("Grand Theft Auto V is an open-world action-adventure game where players control three protagonists who must navigate the criminal underworld of Los Santos and complete missions while avoiding the law and rival gangs.");
		gtaV.setImage(getImageBytesFromFile("gta-v", "jpg"));
		libCollection.insertOne(gtaV);
		
		Item raiders = new Item();
		raiders.setType(ItemType.DVD);
		raiders.setName("Indiana Jones and the Raiders of the Lost Ark");
		raiders.setDescription("Archaeologist and adventurer Indiana Jones is hired by the US government to find the Ark of the Covenant before the Nazis.");
		raiders.setImage(getImageBytesFromFile("indy1", "jpg"));
		libCollection.insertOne(raiders);

		Item templeOfDoom = new Item();
		templeOfDoom.setType(ItemType.DVD);
		templeOfDoom.setName("Indiana Jones and the Temple of Doom");
		templeOfDoom.setDescription("\"Indiana Jones and the Temple of Doom\" - A darker and more exotic sequel that sees Indy battling a cult in India and trying to save enslaved children from a gruesome fate.");
		templeOfDoom.setImage(getImageBytesFromFile("indy2", "jpg"));
		libCollection.insertOne(templeOfDoom);

		Item lastCrusade = new Item();
		lastCrusade.setType(ItemType.DVD);
		lastCrusade.setName("Indiana Jones and the Last Crusade");
		lastCrusade.setDescription("Indiana Jones and his father race against Nazis to find the Holy Grail.");
		lastCrusade.setImage(getImageBytesFromFile("indy3", "jpg"));
		libCollection.insertOne(lastCrusade);

		Item spiderMan = new Item();
		spiderMan.setType(ItemType.DVD);
		spiderMan.setName("Spider-Man");
		spiderMan.setDescription("\"Spiderman\" (2002) - The first big-screen adaptation of the classic comic book superhero, following Peter Parker as he gains spider-like abilities and must use them to stop a nefarious scientist from unleashing a deadly virus.");
		spiderMan.setImage(getImageBytesFromFile("spiderman", "jpg"));
		libCollection.insertOne(spiderMan);

		Item theTrial = new Item();
		theTrial.setType(ItemType.DVD);
		theTrial.setName("The Trial");
		theTrial.setDescription("\"The Trial\" is a surreal and nightmarish adaptation of Franz Kafka's novel, in which a man is arrested and put on trial for a crime that is never specified.");
		theTrial.setImage(getImageBytesFromFile("trial", "jpg"));
		libCollection.insertOne(theTrial);

		Item doubleIndemnity = new Item();
		doubleIndemnity.setType(ItemType.DVD);
		doubleIndemnity.setName("Double Indemnity");
		doubleIndemnity.setDescription("An insurance salesman and a femme fatale plot to kill her husband for his insurance money in this classic film noir.");
		doubleIndemnity.setImage(getImageBytesFromFile("double", "jpg"));
		libCollection.insertOne(doubleIndemnity);

		Item theBigSleep = new Item();
		theBigSleep.setType(ItemType.DVD);
		theBigSleep.setName("The Big Sleep");
		theBigSleep.setDescription("\"The Big Sleep\" - A stylish and convoluted film noir starring Humphrey Bogart as private detective Philip Marlowe, who gets embroiled in a web of murder, blackmail, and deception while investigating a wealthy family's affairs.");
		theBigSleep.setImage(getImageBytesFromFile("big-sleep-mov", "jpg"));
		libCollection.insertOne(theBigSleep);

		Item outOfThePast = new Item();
		outOfThePast.setType(ItemType.DVD);
		outOfThePast.setName("Out of the Past");
		outOfThePast.setDescription("\"Out of the Past\" is a moody and suspenseful film noir about a small-town gas station owner whose past catches up with him when a mysterious woman from his former life reappears, bringing danger and tragedy in her wake.");
		outOfThePast.setImage(getImageBytesFromFile("out-of-the-past", "jpg"));
		libCollection.insertOne(outOfThePast);
		
		Item lighthouse = new Item(ItemType.DVD);
		lighthouse.setName("The Lighthouse");
		lighthouse.setDescription("\"The Lighthouse\" is a haunting psychological thriller set on a remote New England island, where two lighthouse keepers are pushed to their limits by isolation, madness, and supernatural forces.");
		lighthouse.setImage(getImageBytesFromFile("lighthouse", "jpg"));
		libCollection.insertOne(lighthouse);
		
		Item twolane = new Item(ItemType.DVD);
		twolane.setName("Two-Lane Blacktop");
		twolane.setDescription("\"Two-Lane Blacktop\" - An existentialist road movie that explores the ennui and detachment of two car-obsessed drifters as they race aimlessly across the American Southwest, searching for purpose and connection in a world that seems increasingly empty.\"");
		twolane.setImage(getImageBytesFromFile("twolane", "jpg"));
		libCollection.insertOne(twolane);

	}
	
//	private static void testbff() {
//		byte[] arr = getImageBytesFromFile("nfs", "jpg");
//	}
	
	private static byte[] getImageBytesFromFile(String path, String format) {
		path = "../../" + path + "." + format;
		System.out.println("attempting to read image " + path + "...");
		File file = new File(path);
		System.out.println("abs path: ");
		System.out.println(file.getAbsolutePath());
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			System.out.println("ERROR: img file " + path + " not found!");
			e.printStackTrace();
			return null;
		}
		System.out.println("found " + path + " file");
		ByteArrayOutputStream aos =  new ByteArrayOutputStream();
		try {
			ImageIO.write(image, format, aos);
		} catch (IOException e) {
			System.out.println("ERROR: reading img file " + path);
			e.printStackTrace();
			return null;
		}
		System.out.println("got " + path + " bytes");
		byte[] byteArray = aos.toByteArray();
		return byteArray;
	}
}












