package GamePlay;

import GamePlay.PlayStart.CardType;

public class Card {

    private CardType cardTP; 
    private String cardName; 
    
    private int attactPower;
    private int offsentPower;
    
    public Card()
    {
    	cardTP = CardType.MONSTER;
    	cardName ="";
    }
	
	public void setCardType(String tp)
	{
		if(!tp.equals("") && tp!=null)
		{
			if(tp.equals("monster"))
				cardTP = CardType.MONSTER;
			else if(tp.equals("spell"))
				cardTP = CardType.SPELL;
		}
	}
	public CardType getCardType()
	{
		return cardTP;
	}
	
	public void setCardName(String name) 
	{
		if(!name.equals("") && name!=null)
		{
			cardName = name;
		}
	}
	
	public String getCardName()
	{
		return cardName;
		
	}
	
	public void setAttactPower(int powerNum)
	{
		if(powerNum>=0)
			attactPower = powerNum;
	}
	
	public int getAttactPower()
	{
		return attactPower;
	}
	
	public void setOffsentPower(int powNum)
	{
		if(powNum>=0)
			offsentPower = powNum;
	}
	
	public int getOffsentPower()
	{
		return offsentPower;
	}
	
	public static Card copyCard( Card tempCard)
	{
		if(tempCard != null)
		{
			Card newCard = new Card();
			newCard.cardTP = tempCard.getCardType();
			newCard.cardName = tempCard.getCardName();
			if(tempCard.getCardType() == CardType.MONSTER)
			{
				newCard.attactPower = tempCard.getAttactPower();
				newCard.offsentPower = tempCard.getOffsentPower();
			}
			
			return newCard;
		}
		
		return null;
	}
	
	
}
