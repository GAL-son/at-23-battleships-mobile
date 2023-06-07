# Battleship server endpoints description

## Server related endponts
### Server Status
Endpoint used for checking if server is active.
* **Method** - GET
* **Path** - `/api/server`   
* **Request parameters** - none
* **Returns** - boolean value whether server is active or not

## User related endpoints
### User login 
Endpoint user to login to server. 
* **Method** - POST
* **Path** - `/api/login`
* **Request parameters** 
    * Body
        * *login* - user login (`String`)
        * *password* - user password (`String`) 
* **Returns** JSON formatted user data

### User logout
Endpoint used for loging users out
* **Method** - POST
* **Path** - `/api/logout`
* **Request parameters** 
    * Body
        * *login* - user login (`String`)
        * *password* - user password (`String`)         
* **Returns** boolean value whether user was loged out

### User register
Endpoint used for registration of new users
* **Method** - POST
* **Path** - `/api/login`
* **Request parameters** 
    * Body
        * *login* - user login (`String`)
        * *password* - user password (`String`) 
        * *email* - user email (`String`)(*Optional*)
* **Returns** JSON formatted data of newly created user

### Change user password
Endpoint method used for changing user password. User must be logged in.
* **Method** - POST
* **Path** - `/api/changePassword`
* **Request parameters** 
    * Body
        * *login* - user login (`String`)
        * *oldPassword* - old user password (`String`) 
        * *newPassword* -  new user password (`String`)
* **Returns** Boolean value whether password was changed

### Delete user account
Endpoint used for deleting an account
Endpoint used for registration of new users
* **Method** - POST
* **Path** - `/api/delete`
* **Request parameters** 
    * Body
        * *login* - user login (`String`)
        * *password* - user password (`String`) 
* **Returns** Boolean value whether user was deleted or not

### User ranking
Endpoint used for geting player ranking
* **Method** - GET
* **Path** - `/api/ranking`
* **Request parameters** - NONE
* **Returns** JSON formatted data of all users in database (*safe*)

### Player score
Endpoint that returns score of user with given **id**
* **Method** - GET
* **Path** - `/api/stats/{id}`
* **Request parameters**
    * Path
        * *id* - id of user that score will be pulled
* **Returns** score of user with given **id**

## Game related endpoints
### Joining game queue
Endpoint used to join game queue
* **Method** - POST
* **Path** - `/api/game/join`
* **Request parameters**
    * Body
        * *uid* - id of user joining queue
* **Returns** boolean value whether queue was joined or not

### Checking state of game queue
Endpoint used to determine if user has been assigned to a game. 
* **Method** - POST
* **Path** - `/api/game/queue`
* **Request parameters**
    * Body
        * *uid* - id of user checking queue
* **Returns** boolean value whether game has been found or not.

### Seting ships
Endpont used for seting ships when game is starting
* **Method** - POST
* **Path** - `/api/game/set`
* **Request parameters**
    * Body
        * *uid* - id of user checking queue
        * *shipsJsonString* - String containing JSON representation of ship setup.
            Format for seting up ships:
            ```json
            {
                "ships": [
                    {
                        "size": 4,
                        "fieldxy": [
                            0,
                            0
                        ],
                        "vertical": true
                    }, ... 
                ]
            }
            ```
* **Returns** boolean value whether ships were set correctly

### GetEnemyBoard
Endpoint for getting ships of player's opponent. (**APP ONLY**)
* **Method** - GET
* **Path** - `/api/game/start`
* **Request parameters**
    *  *uid* - id of user pooling for his opponent's ships
* **Returns** JSON formatted ship setup[^1]

[^1]: Same as seting ships

### Pooling game state
Endpoint method used for pooling current game state. 
* **Method** - GET
* **Path** - `/api/game/state`
* **Request parameters**
    * Body
        * *uid* - id of user pooling for state
* **Returns** JSON formatted state of current game:
    ```json
     {
       "turnId":1,
       "gid":0,
       "opponent":
           {
           "score":0,
           "login":"gal_son"
           },
       "isStarted":true,
       "isFinished":false,
       "player":{
           "score":0,
           "login":"test13"
           }
       }
    ```

### Making move
Endpoint used for making a move when game is started
* **Method** - POST
* **Path** - `/api/game/move`
* **Request parameters**
    * Body
        * *uid* - id of user pooling for state
        * *x* - x position of move (0-9)
        * *y* - y position of move (0-9)
* **Returns** boolean value whether move (shot) was succesfull or not (enemy ship was hit)

### Leaving a game
Endpoint method used for leaving the game
* **Method** - POST
* **Path** - `/api/game/leave`
* **Request parameters**
    * Body
        * *uid* - id of user leaving the game
* **Returns** boolean value whether the game was left