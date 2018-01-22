import java.awt.Graphics2D;

import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.NPCS;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.Dialogues;
import org.osbot.rs07.api.Walking;


@ScriptManifest(author = "Themat7", info = "My first script", name = "Beer Buyer", version = 0, logo = "")
public class Beer extends Script {
	 
	

	@Override
	public void onStart() {
		log("Welcome to Shitty thing by Themat6.");
		log("This is a test script and is available for free on github.");
		log("Enjoy the script, gain some beer merch cash!.");
	}
	
	 int buyPrice = 2;
	 int sellPrice = 62;
	
	 static int beerBought ;
//Tracks profit and number of beers

private enum State{
	BUY, BANK, WALK
};

//Deposit box and bar
private final Area BeerBuy = new Area(3044,3255,3054,3259);//two x and y coords
private final Area Bank = new Area(3043,3237,3047,3233);//two x and y coords
//square area  will put either bank or bartender on screen when walked too
private State getState(){
	inventory.getCapacity();
	if(BeerBuy.contains(myPlayer().getPosition())&& !inventory.isFull()) {//If we are in bar and not full. BUY
		return State.BUY;
	}
	else if( !Bank.contains(myPlayer().getPosition()) ||!BeerBuy.contains(myPlayer().getPosition() )) {
		return State.WALK;

		}
	//if we are full or we aren't in the bank or in the bar WALK
	else{
	return State.BANK; //if we are full but at the bank
}
	}
@Override
public int onLoop() throws InterruptedException {
	switch (getState()) {
	case BUY:
		NPC bartender = npcs.closest(1313);
		inventory.getCapacity();

        if(!dialogues.inDialogue()&& bartender.isVisible())//WE AINT TALKING? Also is he visible (would spam i think)
        {
            bartender.interact("Talk-to");//Start talking
            sleep(random(700,800));
            }
            else if(dialogues.inDialogue()){
        	dialogues.completeDialogue("Could I buy a beer please?");//continues and then completes with given text
             beerBought++;//adds count every buy
        sleep(random(200,700));
        
            }

        
        else{
        	getCamera().toEntity(bartender);//if you can't see him move camera to bartender
        }


        break;
		
		
	case WALK:
			if(!Bank.contains(myPlayer().getPosition() )&&inventory.isFull()){//if not at bank but inventory full
		getWalking().walk(Bank);//go to the bank
		}
			
			
		else if (!BeerBuy.contains(myPlayer().getPosition())&&!inventory.isFull()){//if not at bar and not full
			getWalking().walk(BeerBuy);//go to the bar

			}
	
		break;
	case BANK:
		if(inventory.contains("Beer")){//double check we have inventory
		depositBox.open();
		sleep(random(200,500));
		depositBox.depositAllExcept("Coins");//empty inventory
		sleep(random(200,500));
		depositBox.close();
		}
		
		
		break;
	}
	return random(700, 800);
}
	

@Override
public void onPaint(Graphics2D rg) {

	int totalProfit =  (sellPrice - buyPrice) * beerBought;

	rg.drawString("Beer " + beerBought, 80, 80);
	rg.drawString("Our profit is " + totalProfit +"GP" , 50, 50);
//updates as we go this
	//api is awesome

}
}