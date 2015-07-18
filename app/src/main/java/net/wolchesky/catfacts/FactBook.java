package net.wolchesky.catfacts;

import java.util.Random;

/**
 * Created by cppxw on 9/15/2014.
 */
public class FactBook {

    public String[] mFacts = {
            "A cat uses its whiskers for measuring distances.  The whiskers of a cat are capable of registering very small changes in air pressure.",
            "Cats lose almost as much fluid in the saliva while grooming themselves as they do through urination.",
            "Cats take between 20-40 breaths per minute.",
            "It has been scientifically proven that stroking a cat can lower one's blood pressure.",
            "Most cats killed on the road are un-neutered toms, as they are more likely to roam further afield and cross busy roads.",
            "It has been scientifically proven that owning cats is good for our health and can decrease the occurrence of high blood pressure and other illnesses.",
            "A cat's whiskers are thought to be a kind of radar, which helps a cat gauge the space it intends to walk through.",
            "Normal body temperature for a cat is 102 degrees F.",
            "When your cats rubs up against you, she is actually marking you as \"hers\" with her scent. If your cat pushes his face against your head, it is a sign of acceptance and affection.",
            "Cats with long, lean bodies are more likely to be outgoing, and more protective and vocal than those with a stocky build."
    };

    public String getFact() {
        String fact = "";
        //Randomly select a fact
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(mFacts.length);
        fact = mFacts[randomNumber];

        return fact;
    }
}
