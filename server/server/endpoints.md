# Endpoints description

### LOGIN
* [Endpoint] POST /api/login
* [PARAMS] 
    - [BODY] String login, String password
* [RETURNS] USER JSON

### REGISTER
* [ENDPOINT] PUT /api/register
* [PARAMS] 
    - [BODY]String login, String password, String email (can be empty)
* [RETURNS] USER JSON

### LOGOUT
* [ENDPOINT] POST /api/logout
* [PARAMS]
     - [BODY] String login, String password
* [RETURNS] True if logedout

### RANKING
* [ENDPONT] GET /api/ranking
* [PARAMS]
* [RETURNS] List USER JSON

### PLAYER SCORE
* [ENDPOINT] GET /api/stats/{id}
* [PARAMS] {id} - user id
* [RETURNS] SCORE OF PLAYER
