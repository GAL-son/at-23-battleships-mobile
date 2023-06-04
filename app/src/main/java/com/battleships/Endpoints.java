package com.battleships;

public enum Endpoints {
        LOGIN("/api/login"), //POST
        REGISTER("/api/register"), //POST
        RANKING("/api/ranking"), //GET
        SERVER("/api/server"), //GET
        LOGOUT("api/logout"), //POST
        GAME_JOIN("api/game/join"), //POST
        GAME_QUEUEQ("/api/game/queue"), //POST
        GAME_SET("api/game/set"), //POST
        GAME_START("api/game/start"), //GET
        GAME_STATE("api/game/state"), //GET
        GAME_MOVE("/api/game/move"); //POST

        private final String endpoint;

        Endpoints(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getEndpoint() {
            return endpoint;
        }
}
