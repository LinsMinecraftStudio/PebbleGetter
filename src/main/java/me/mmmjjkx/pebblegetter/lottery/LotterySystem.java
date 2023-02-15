package me.mmmjjkx.pebblegetter.lottery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LotterySystem {
    public static Award lottery(List<Award> awardList) {
        if(awardList.isEmpty()){
            return null;
        }
        int size = awardList.size();

        double sumProbability = 0d;
        for (Award award : awardList) {
            sumProbability += award.getProbability();
        }

        List<Double> sortAwardProbabilityList = new ArrayList<>(size);
        double tempSumProbability = 0d;
        for (Award award : awardList) {
            tempSumProbability += award.getProbability();
            sortAwardProbabilityList.add(tempSumProbability / sumProbability);
        }

        double randomDouble = Math.random();
        sortAwardProbabilityList.add(randomDouble);
        Collections.sort(sortAwardProbabilityList);
        int lotteryIndex = sortAwardProbabilityList.indexOf(randomDouble);
        return awardList.get(lotteryIndex);
    }
}
