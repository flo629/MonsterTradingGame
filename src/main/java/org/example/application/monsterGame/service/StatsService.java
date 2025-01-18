package org.example.application.monsterGame.service;

import org.example.application.monsterGame.entity.Stats;
import org.example.application.monsterGame.repository.StatsDbRepository;
import org.example.application.monsterGame.repository.UserRepository;

public class StatsService {

    private final StatsDbRepository statsDbRepository;

    public StatsService(StatsDbRepository statsDbRepository) {
        this.statsDbRepository = statsDbRepository;
    }

    public Stats getStats(String username){
        var stats = statsDbRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return stats;
    }

}
