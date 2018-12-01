package com;

import java.util.List;

public interface PointService {

    /**
     * Время до того как появится возможность передать поинты другому игроку в минутах.
     */
    long minutesUntilGainPoint(String nickname);

    /**
     * Возвращает отсортированный список игроков по количеству поинтов. От большего к меньшему
     */
    List<String> topByPoints();

    /**
     * Получить количество поинтов у выбранного игрока.
     */
    int getPoints(String player);

    /**
     * Передать поинты игроку. Если первый игрок может совешить транзацию, то у него отнимутся поинты и передатутся
     * другому игроку
     *
     * Имеет кулдаун
     */
    boolean transferPoints(String fromPlayer, String toPlayer, int num);

    /**
     * Дать поинты игроку. У первого игрока они не отнимаются.
     *
     * Имеет кулдаун
     */
    boolean addPoints(String fromNickname, String toNickname, int count);

    /**
     * Добавить поинты игроку без проверок на невозможность операции.
     */
    void addPoints(String player, int qty);

    /**
     * Установить точное количество поинтов выбранному игроку
     */
    void setPoints(String player, int qty);
}
