package GamePlay;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class PlayStart {

	static DataOutputStream toServer;
	static DataInputStream fromServer;

	static DataOutputStream toClient;
	static DataInputStream fromClient;

	static ArrayList<Card> playerCardTypeList = new ArrayList<Card>();
	static ArrayList<Card> server_deck = new ArrayList<Card>();
	static ArrayList<Card> server_hand = new ArrayList<Card>();
	static ArrayList<Card> server_fieldzone = new ArrayList<Card>();
	static int serverbloodsNum = 8000;

	static ArrayList<Card> client_deck = new ArrayList<Card>();
	static ArrayList<Card> client_hand = new ArrayList<Card>();
	static ArrayList<Card> client_fieldzone = new ArrayList<Card>();
	static int clientbloodsNum = 8000;
	
	static int bloodsNum = 8000;

	static String commandString[] = { "GETHANDCARD", "YOURTURN", "ATTRACTCMD","GETFIELDZONE","FINISHDECK" };

	static int deckCount = 40;

	static enum CardType {
		MONSTER, SPELL;
	}

	static enum RoleType {
		SERVER, CLIENT;
	}

	static RoleType curroleTP;
	
	
	public static void loseGame()
	{
		DataOutputStream curToStream = null;
		//DataInputStream curFromStream = null;
		
		if (curroleTP == RoleType.SERVER) {
			curToStream = toClient;
			//curFromStream = fromClient;

		} else if (curroleTP == RoleType.CLIENT) {
			curToStream = toServer;
			//curFromStream = fromServer;
		}
		
		try {
			curToStream.writeInt(2222);
			curToStream.writeUTF(commandString[4]);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
	}
	
	
	public static void chooseMyCard( int indexCard )
	{
		ArrayList<Card>  fieldZoneCardlist = null;
		DataOutputStream curToStream = null;
		DataInputStream curFromStream = null;
		
		
		
		if (curroleTP == RoleType.SERVER) {
			
			fieldZoneCardlist = server_fieldzone;
			curToStream = toClient;
			curFromStream = fromClient;

		} else if (curroleTP == RoleType.CLIENT) {
			
			fieldZoneCardlist = client_fieldzone;
			curToStream = toServer;
			curFromStream = fromServer;
		}
		
		if(fieldZoneCardlist != null)
		{
			if(fieldZoneCardlist.size() >= indexCard)
			{
				BufferedReader strin = new BufferedReader(
						new InputStreamReader(System.in));
				System.out.print("input attact card Number(attact *):");
				
				try {
					String  attactCmdString = strin.readLine();
					String attacts[] = attactCmdString.split(" ");
					if(attacts[0].equals("attact"))
					{
						int attactNum = Integer.parseInt(attacts[1]);
						curToStream.writeInt(2222);
						curToStream.writeUTF(commandString[2]);
						curToStream.writeInt(attactNum);
						Card  myCard = fieldZoneCardlist.get(attactNum-1);
						curToStream.writeInt(myCard.getAttactPower());
		
						int offsentPowe = curFromStream.readInt();
						
						if(myCard.getAttactPower() <= offsentPowe)
						{
							fieldZoneCardlist.remove(indexCard-1);
						}
						
						
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}
		
	}
	
	public static void showOppentFieldZone()
	{
		DataOutputStream curToStream = null;
		DataInputStream curFromStream = null;
		if (curroleTP == RoleType.SERVER) {
			curToStream = toClient;
			curFromStream = fromClient;

		} else if (curroleTP == RoleType.CLIENT) {
			
			curToStream = toServer;
			curFromStream = fromServer;
		}
		System.out.println("showOppentFieldZone");

		if (curToStream != null && curFromStream != null) {
			try {
				curToStream.writeInt(2222);
				curToStream.writeUTF(commandString[3]);

				int handCount = curFromStream.readInt();
				System.out.println(handCount);
				if (handCount > 0) {
					for (int i = 0; i < handCount; i++) {
						System.out.println(i + 1 +"  "+ curFromStream.readUTF());
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}
	
	
	
	public static void showMyFieldZone()
	{
		ArrayList<Card>  fieldZoneCardlist = null;
		if (curroleTP == RoleType.SERVER) {
			
			fieldZoneCardlist = server_fieldzone;

		} else if (curroleTP == RoleType.CLIENT) {
			
			fieldZoneCardlist = client_fieldzone;
		}
		
		if(fieldZoneCardlist != null)
		{
			for(int i=0; i < fieldZoneCardlist.size(); i++)
			{
				System.out.println(i+1 +"  "+ fieldZoneCardlist.get(i).getCardName());
			}
		}
		
	}
	
	
	public static void getACardToFildZone(int index)
	{
		ArrayList<Card>  handCardlist = null;
		ArrayList<Card>  filedZoneCardlist = null;
		if (curroleTP == RoleType.SERVER) {
			handCardlist = server_hand;
			filedZoneCardlist = server_fieldzone;

		} else if (curroleTP == RoleType.CLIENT) {
			
			handCardlist = client_hand;
			filedZoneCardlist = client_fieldzone;
		}
		
		if(handCardlist !=null)
		{
			if(index <= handCardlist.size())
			{
				filedZoneCardlist.add(handCardlist.get(index-1));
				handCardlist.remove(index-1);
			}
		}
	}

	public static void showOppentHandCard() {
		DataOutputStream curToStream = null;
		DataInputStream curFromStream = null;
		if (curroleTP == RoleType.SERVER) {
			curToStream = toClient;
			curFromStream = fromClient;

		} else if (curroleTP == RoleType.CLIENT) {
			
			curToStream = toServer;
			curFromStream = fromServer;
		}
		System.out.println("showOppentHandCard");

		if (curToStream != null && curFromStream != null) {
			try {
				curToStream.writeInt(2222);
				curToStream.writeUTF(commandString[3]);

				int handCount = curFromStream.readInt();
				//System.out.println(handCount);
				if (handCount > 0) {
					for (int i = 0; i < handCount; i++) {
						System.out.println(i +1  + "  " +  curFromStream.readUTF());
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	public static void getACardFromDeck() {

		if (curroleTP == RoleType.SERVER) {

			Card newCard = server_deck.get(0);
			if (newCard.getCardType() == CardType.MONSTER) {
				server_hand.add(server_deck.get(0));
				server_deck.remove(0);
			} else if (newCard.getCardType() == CardType.SPELL) {
				server_deck.remove(0);
				for(int i = 0,j=0;i<2 && j < server_deck.size();)
				{
					Card tempCard = server_deck.get(j);
					if(tempCard.getCardType() == CardType.MONSTER)
					{
						server_hand.add(server_deck.get(j));
						server_deck.remove(j);
						i++;
						j++;
					}else if(tempCard.getCardType() == CardType.SPELL)
					{
						j++;
					}
				}
	
			}

		} else if (curroleTP == RoleType.CLIENT) {
			Card newCard = client_deck.get(0);
			if (newCard.getCardType() == CardType.MONSTER) {
				client_hand.add(client_deck.get(0));
				client_deck.remove(0);
			} else if (newCard.getCardType() == CardType.SPELL) {
//				client_deck.remove(0);
//				client_hand.add(server_deck.get(0));
//				client_deck.remove(0);
//				client_hand.add(server_deck.get(0));
//				client_deck.remove(0);
				client_deck.remove(0);
				for(int i = 0,j=0;i<2 && j<client_deck.size();)
				{
					Card tempCard = client_deck.get(j);
					if(tempCard.getCardType() == CardType.MONSTER)
					{
						client_hand.add(client_deck.get(j));
						client_deck.remove(j);
						i++;
						j++;
					}else if(tempCard.getCardType() == CardType.SPELL)
					{
						j++;
					}
				}
				
				
				
			}
		}
	}

	public static void removeAOpponetDeckCard() {
		if (curroleTP == RoleType.SERVER) {
			client_hand.add(client_hand.get(0));
			client_hand.remove(0);

		} else if (curroleTP == RoleType.CLIENT) {
			server_hand.add(server_deck.get(0));
			server_deck.remove(0);

		}
	}

	public static void showDeckCard() {

		ArrayList<Card> tempCardList = null;
		if (curroleTP == RoleType.SERVER) {
			tempCardList = server_deck;
		} else if (curroleTP == RoleType.CLIENT) {
			tempCardList = client_deck;
		}
		System.out.println("showDeckCard ==" + tempCardList.size());
		for (int i = 0; i < tempCardList.size(); i++) {
			Card tempCard = tempCardList.get(i);

			System.out.println(i + " " + tempCard.getCardName());
		}
	}

	public static void showOpponnetDeck() {
		System.out.println("show Oppent Deck card");
		ArrayList<Card> tempCardList = null;
		if (curroleTP == RoleType.SERVER) {
			tempCardList = client_deck;
		} else if (curroleTP == RoleType.CLIENT) {
			tempCardList = server_deck;
		}
		System.out.println("showOpponnetDeckCard ==" + tempCardList.size());
		for (int i = 0; i < tempCardList.size(); i++) {
			Card tempCard = tempCardList.get(i);

			System.out.println(i + "  " + tempCard.getCardName());
		}
	}

	public static void showMyHand() {
		ArrayList<Card> tempCardList = null;
		if (curroleTP == RoleType.SERVER) {
			tempCardList = server_hand;
		} else if (curroleTP == RoleType.CLIENT) {
			tempCardList = client_hand;
		}

		for (int i = 0; i < tempCardList.size(); i++) {
			Card tempCard = tempCardList.get(i);
			System.out.println(i + 1 + "  " + tempCard.getCardName());
		}
	}

	public static void processCommand(String cmd) {
		if (cmd.equals("show deck card")) {
			showDeckCard();
		
		} else if (cmd.equals("show op deck")) {
			showOpponnetDeck();
			
		} else if (cmd.equals("show my hand")) {
			showMyHand();
			
		} else if (cmd.equals("get a card")) {
			getACardFromDeck();
			
		} else if (cmd.equals("show op hand")) {
			showOppentHandCard();
			
		}else if (cmd.startsWith("summon monster")) {
			String cmdString[] = cmd.split(" ");
			int index = Integer.parseInt(cmdString[2]);
			if(index > 0)
			{
				getACardToFildZone(index);
			}
			
		}  else if (cmd.startsWith("choose my card")) {
			String cmdString[] = cmd.split(" ");
			int index = Integer.parseInt(cmdString[3]);
			if(index > 0)
			{
				chooseMyCard(index);
			}
			
		}else if (cmd.equals("show op bf")) {
			showOppentFieldZone();
			
		} else if (cmd.equals("show my bf")) {
			showMyFieldZone();
			
		}else {
			System.out.println(cmd);
		}
	}

	public static Card gernertCardByCardName(String name) {
		for (int i = 0; i < playerCardTypeList.size(); i++) {
			Card tempCard = playerCardTypeList.get(i);
			if (tempCard.getCardName().equals(name)) {
				Card newCard = Card.copyCard(tempCard);
				return newCard;
			}
		}
		return null;
	}

	public static void processCardType(String filename) {
		try {

			String fileFullName = filename + ".txt";
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(fileFullName));
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();

			Card tmpcard = new Card();
			tmpcard.setCardType(line);
			line = br.readLine();
			tmpcard.setCardName(line);
			line = br.readLine();
			tmpcard.setAttactPower(Integer.parseInt(line));
			line = br.readLine();
			tmpcard.setOffsentPower(Integer.parseInt(line));

			playerCardTypeList.add(tmpcard);

			br.close();

		} catch (Exception e) {
			System.err.println(e);
		}

	}

	public static void buildDeckList() {
		ArrayList<Card> tempCardList = null;
		if (curroleTP == RoleType.SERVER) {
			tempCardList = server_deck;
		} else if (curroleTP == RoleType.CLIENT) {
			tempCardList = client_deck;
		}

		try {
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream("myDeck.txt"));
			BufferedReader br = new BufferedReader(reader);

			String line = br.readLine();
			while (line != null) {
				processCardType(line);
				line = br.readLine();
			}
			br.close();

		} catch (Exception e) {
			System.err.println(e);
		}

		int cardTypeNum = playerCardTypeList.size();

		if (cardTypeNum > 0) {
			for (int i = 0; i < deckCount; i++) {
				// Random r = new Random(System.currentTimeMillis());
				// System.out.println(r.nextInt(cardTypeNum));
				int index = (int) (Math.random() * cardTypeNum);
				Card randCard = playerCardTypeList.get(index);
				Card copyCard = Card.copyCard(randCard);
				tempCardList.add(copyCard);
			}
		}

	}

	public static void client() {

		try {
			// Create a socket and try to connect to the server
			Socket socket = new Socket("localhost", 8126);

			// Create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());

			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());
			System.out.println("Duel start! Sending deck to server !");
			toServer.writeInt(10);
			;
			int success = fromServer.readInt();

			if (success == 10) {

				curroleTP = RoleType.CLIENT;
				System.out.println("My Role is client!");
				buildDeckList();
			}

			for (int i = 0; i < client_deck.size(); i++) {
				toServer.writeUTF(client_deck.get(i).getCardName());
			}

			for (int i = 0; i < deckCount; i++) {
				String name = fromServer.readUTF();
				if (gernertCardByCardName(name) != null) {
					server_deck.add(gernertCardByCardName(name));
				}
			}

			for (int j = 0, i =0; j < 5 && i <client_deck.size(); ) {
				
				Card  tempCard = client_deck.get(i);
				if(tempCard.getCardType() == CardType.MONSTER)
				{
					client_hand.add(client_deck.get(i));
					client_deck.remove(i);
					j++;
					i++;
				}else if(tempCard.getCardType() == CardType.SPELL)
				{
					i++;
				}
//				server_hand.add(server_deck.get(i));
//				server_deck.remove(i);
				
			}

			BufferedReader strin = new BufferedReader(new InputStreamReader(
					System.in));
			
			toServer.writeInt(1111);
			
			System.out.println("My hand Card:");
			
			showMyHand();
			
			System.out.print("summon monster a Card( summon monster *):");
			
			String monsterString = strin.readLine();
			processCommand(monsterString);
			
			

			while (true) {
				// Get the radius from the user
				// System.out.print( "Please enter command: " );
				// String command = keyboard.nextLine();
				// System.out.println(command);
				// Send the radius to the server
				// toServer.writeUTF(command);
				// toServer.flush();

				// String recieve = fromServer.readUTF(); // BLOCKING !!!!!!

				// Display to the text area
				// System.out.println( "recieve ");
				
				int serverCmd = fromServer.readInt();
				System.out.println(serverCmd);
				
				if(serverCmd==1111)
				{
				
					if(client_deck.size() >0 )
					{
						if(bloodsNum <=0)
						{
							System.out.println("You lost!");
							loseGame();
							break;
						}
						getACardFromDeck();
						
					}
					else
					{
						System.out.println("You lost!");
						loseGame();
						break;
					}
					
					
                   try {
						while (true) {
							System.out.println("bloods ramaind: " +bloodsNum);
							System.out.print("Input Your Command:");
							String str = strin.readLine();
							if (str != null) {
								if (str.equalsIgnoreCase("end my term")) {
									toServer.writeInt(1111);
									break;

								} else {
									processCommand(str);
									
								}
							}
						}
						
					}catch (IOException e) {
							e.printStackTrace();
						}
					
				}else if(serverCmd==2222){
					
					String recevrSerCmd = fromServer.readUTF();
					System.out.println(recevrSerCmd);
					if(recevrSerCmd.equals(commandString[0]))
					{
						toServer.writeInt(client_hand.size());
						for(int i =0; i < client_hand.size();i++)
						{
							toServer.writeUTF(client_hand.get(i).getCardName());
						}
					}else if(recevrSerCmd.equals(commandString[2])){
						
						int cardIndex = fromServer.readInt();
						if(cardIndex <= client_fieldzone.size() && cardIndex >0)
						{
							Card cardNum = client_fieldzone.get(cardIndex-1);
							int attactNum = fromServer.readInt();
							toServer.writeInt(cardNum.getOffsentPower());
							
							if(cardNum.getOffsentPower() <= attactNum)
							{
								client_fieldzone.remove(cardIndex-1);
								bloodsNum -= (attactNum -cardNum.getOffsentPower());
							}	
						}else
						{
							int attactNum = fromServer.readInt();
							toServer.writeInt(0);
							bloodsNum -= attactNum;
							
						}
						
					}else if(recevrSerCmd.equals(commandString[3])){
						
						toServer.writeInt(client_fieldzone.size());
						for(int i =0; i < client_fieldzone.size();i++)
						{
							toServer.writeUTF(client_fieldzone.get(i).getCardName());
						}
					}else if(recevrSerCmd.endsWith(commandString[4]))
					{
						System.out.println("Congratulations! You Win!");
						break;
					}
						
				}
				
				
					

					

			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	public static void server() {
		System.out.println("Server started at " + new Date());

		try {
			// Create a server socket that will "listen" on port 8123
			ServerSocket serverSocket = new ServerSocket(8126);
			//

			// Listen for a connection request
			Socket socket = serverSocket.accept(); // BLOCKING !!!!!!!

			System.out.println("Accepted incoming connection");

			// Create data input and output streams
			fromClient = new DataInputStream(socket.getInputStream());

			toClient = new DataOutputStream(socket.getOutputStream());
			int size = fromClient.readInt();
			System.out.println(size);

			if (size == 10) {
				toClient.writeInt(10);
				curroleTP = RoleType.SERVER;
				System.out.println("My Role is Server!");
				buildDeckList();
			}

			for (int i = 0; i < deckCount; i++) {
				String name = fromClient.readUTF();
				if (gernertCardByCardName(name) != null) {
					client_deck.add(gernertCardByCardName(name));
				}

			}

			for (int i = 0; i < server_deck.size(); i++) {
				toClient.writeUTF(server_deck.get(i).getCardName());
			}

			for (int j = 0, i=0; j < 5 && i < server_deck.size(); j++) {
//				client_hand.add(client_deck.get(j));
//				client_deck.remove(j);
				Card tempServerCard = server_deck.get(i);
				if(tempServerCard.getCardType() == CardType.MONSTER)
				{
					server_hand.add(server_deck.get(i));
					server_deck.remove(i);
					i++;
					j++;
				}else if(tempServerCard.getCardType() == CardType.SPELL)
				{
					i++;
				}
				
			}

			
			System.out.println(" Games begin!");

			while (true) {
				
				
		        int clientCmd = fromClient.readInt();
		        System.out.println(clientCmd);
		        if(clientCmd == 1111)
		        {
		        	if(server_deck.size() >0 )
					{
		        		if(bloodsNum <=0)
						{
							System.out.println("You lost!");
							loseGame();
							break;
						}
						getACardFromDeck();
						
					}
		        	else
		        	{
		        		System.out.println("You lost!");
		        		loseGame();
		        		break;
		        		
		        	}
		        	
		        	
		        	
		        	try {
						BufferedReader strin = new BufferedReader(
								new InputStreamReader(System.in));

						while (true) {
							System.out.println("bloods ramaind: " +bloodsNum);
							System.out.print("Input Your Command:");
							String str = strin.readLine();
							if (str != null) {
								if (str.equalsIgnoreCase("end my term")) {
									
									toClient.writeInt(1111);
									break;

								} else{
								    processCommand(str);
								}
							}
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
		        	
		        }
		        else if (clientCmd == 2222)
		        {
		        	String recevrSerCmd = fromClient.readUTF();
					System.out.println(recevrSerCmd);
					if(recevrSerCmd.equals(commandString[0]))
					{
						toClient.writeInt(server_hand.size());
						for(int i =0; i < server_hand.size();i++)
						{
							toClient.writeUTF(server_hand.get(i).getCardName());
						}
					}else if(recevrSerCmd.equals(commandString[2])){
						
						int cardIndex = fromClient.readInt();
						if(cardIndex <= server_fieldzone.size() && cardIndex >0)
						{
							Card cardNum = server_fieldzone.get(cardIndex-1);
							int attactNum = fromClient.readInt();
							toClient.writeInt(cardNum.getOffsentPower());
							
							if(cardNum.getOffsentPower() <= attactNum)
							{
								server_fieldzone.remove(cardIndex-1);
								bloodsNum -= (attactNum -cardNum.getOffsentPower());
							}
								
						}
						else   //if card not exit , blood minus attactNum;
						{
							int attactNum = fromClient.readInt();
							toClient.writeInt(0);
							bloodsNum -= attactNum;
						}
					}else if(recevrSerCmd.equals(commandString[3])){
						
						toClient.writeInt(server_fieldzone.size());
						for(int i =0; i < server_fieldzone.size();i++)
						{
							toClient.writeUTF(server_fieldzone.get(i).getCardName());
						}
					}else if(recevrSerCmd.endsWith(commandString[4]))
					{
						System.out.println("Congratulations! You Win!");
						break;
						
					}
		        	
		        }

			}
			
			
			
		}
		catch (IOException ex) {
			System.err.println(ex);
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {

			BufferedReader strin = new BufferedReader(new InputStreamReader(
					System.in));
			System.out.print("input server or client£º");
			String str = strin.readLine();
			if (str.equalsIgnoreCase("server"))
				server();
			else
				client();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
