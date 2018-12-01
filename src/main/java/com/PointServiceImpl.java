package com;

import org.bukkit.configuration.file.YamlConfiguration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PointServiceImpl implements PointService {

    private final String NEXT_USE_PREFIX = "next.";
    private final long DELAY_IN_MINUTES;

    private final YamlConfiguration tempData;
    private final YamlConfiguration points;

    PointServiceImpl(KarmaPoints plugin) {
        tempData = plugin.getTempData();
        points = plugin.getPoints();
        DELAY_IN_MINUTES = plugin.getConfiguration().getLong("delay-in-minutes");
    }

    public long minutesUntilGainPoint(String nickname) {
        String nextUse = tempData.getString(NEXT_USE_PREFIX + nickname);
        if (nextUse == null)
            return -1L;
        LocalDateTime next = LocalDateTime.parse(nextUse);
        return next.isAfter(LocalDateTime.now()) ? Duration.between(LocalDateTime.now(), next).toMinutes() : -1L;
    }

    public List<TopElement> topByPoints() {
        return points.getValues(false)
                .entrySet().stream()
                .map(this::intValueMapper)
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .map(x -> new TopElement(x.getKey(), x.getValue()))
                .collect(Collectors.toList());
    }

    public int getPoints(String player) {
        return points.getInt(player);
    }

    public boolean transferPoints(String fromPlayer, String toPlayer, int num) throws DelayException {
        if (num <= 0) return false;
        int from = points.getInt(fromPlayer);
        if (from - num < 0) return false;
        checkDelay(fromPlayer);
        updateDelay(fromPlayer);
        points.set(fromPlayer, from - num);
        points.set(toPlayer, points.getInt(toPlayer) + num);
        return true;
    }

    public boolean addPoints(String fromNickname, String toNickname, int count) throws DelayException {
        checkDelay(fromNickname);
        updateDelay(fromNickname);
        points.set(toNickname, points.getInt(toNickname) + count);
        return true;
    }

    public void addPoints(String player, int qty) {
        points.set(player, (points.getInt(player) + qty));
    }

    public void setPoints(String player, int qty) {
        points.set(player, qty);
    }

    private void updateDelay(String nickname) {
        tempData.set(NEXT_USE_PREFIX + nickname, LocalDateTime.now().plusMinutes(DELAY_IN_MINUTES).toString());
    }

    private void checkDelay(String nickname) throws DelayException {
        String nextUse = tempData.getString(NEXT_USE_PREFIX + nickname);
        if (nextUse != null) {
            LocalDateTime next = LocalDateTime.parse(nextUse);
            if (next.isAfter(LocalDateTime.now()))
                throw new DelayException(Duration.between(LocalDateTime.now(), next).toMinutes());
        }
    }

    private Map.Entry<String, Integer> intValueMapper(Map.Entry<String, Object> entry) {
        return new AbstractMap.SimpleEntry<>(entry.getKey(), (int) entry.getValue());
    }
}
