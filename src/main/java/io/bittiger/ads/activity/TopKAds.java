package io.bittiger.ads.activity;

import io.bittiger.ads.model.Ad;

import java.util.*;

public class TopKAds {
    private static TopKAds instance = null;
    protected TopKAds() {

    }
    public static TopKAds getInstance() {
        if (instance == null) {
            instance = new TopKAds();
        }
        return instance;
    }
    public List<Ad> selectTopKAds(List<Ad> rankedAds, int k) {
        LinkedList<Ad> selectedAds = new LinkedList<Ad>();
        if (rankedAds == null || rankedAds.size() == 0) {
            return selectedAds;
        }
        PriorityQueue<Ad> minHeap = new PriorityQueue<Ad>(k, new Comparator<Ad>() {
            public int compare(Ad o1, Ad o2) {
                if (o1.getRankScore() == o2.getRankScore()) {
                    return 0;
                }
                return o1.getRankScore() > o2.getRankScore() ? -1 : 1;
            }
        });
        int i = 0;
        while (i < rankedAds.size() && i < k) {
            minHeap.add(rankedAds.get(i++));
        }
        if (k < rankedAds.size()) {
            while (i < rankedAds.size()) {
                Ad cur = rankedAds.get(i);
                if (cur.getRankScore() > minHeap.peek().getRankScore()) {
                    minHeap.poll();
                    minHeap.offer(cur);
                }
                i++;
            }
        }
        while (!minHeap.isEmpty()) {
            selectedAds.addFirst(minHeap.poll());
        }

        return selectedAds;
    }
}
