package com.battleships;

public enum Endpoints {
        LOGIN("/api/login"),
        REGISTER("/api/register"),
        RANKING("/api/ranking"),
        SERVER("/api/server"),
        LOGOUT("api/logout"),
        GAME_JOIN("api/game/join"),
        GAME_QUEUEQ("/api/game/queue"),
        GAME_SET("api/game/set"),
        GAME_START("api/game/start"),
        GAME_STATE("api/game/state"),
        GAME_MOVE("/api/game/move");

        private final String endpoint;

        Endpoints(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getEndpoint() {
            return endpoint;
        }
}
