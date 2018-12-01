package com;

import java.util.List;

public interface PointService {

    /**
     * Время до того как появится возможность передать поинты другому игроку в минутах.
     */
    int minutesUntilGainPoint(String nickname);

    /**
     * Возвращает отсортированный список игроков по количеству поинтов. От большего к меньшему
     */
    List<String> topByPoint();

    /**
     * Получить количество поинтов у выбранного игрока.
     */
    int getPoints(String player);

    /**
     * Передать поинты игроку. Если первый игрок может совешить транзацию, то у него отнимутся поинты и передатутся
     * другому игроку
     */
    boolean transferPoints(String name, String player, int num);

    /**
     * Дать поинты игроку. У первого игрока они не отнимаются, но проверяется, может ли он совершить транзакцию.
     */
    boolean addPoints(String fromNickname, String toNickname, int count);

    /**
     * Добавить поинты игроку без проверок на невозможность операции.
     */
    boolean addPoints(String player, int qty);

    /**
     * Установить точное количество поинтов выбранному игроку
     */
    boolean setPoints(String player, int qty);
}
