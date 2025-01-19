package org.example.application.monsterGame.service;

import org.example.application.monsterGame.controller.ScoreBoardController;
import org.example.application.monsterGame.entity.Stats;
import org.example.application.monsterGame.repository.ScoreBoardRepository;

import java.util.List;

public class ScoreBoardService {

    private final ScoreBoardRepository scoreBoardRepository;

    public ScoreBoardService(ScoreBoardRepository scoreBoardRepository) {
        this.scoreBoardRepository = scoreBoardRepository;
    }

    public List<Stats> getScoreboard() {
        return scoreBoardRepository.getScoreboard();
    }
}
