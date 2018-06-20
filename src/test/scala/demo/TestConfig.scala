package demo

/**
  * The main goal of the object is to be a single access point to test properties like:
  * - qa/uat/prod environments
  * - some enter/exit conditions
  * - etc...
  * Values are hardcoded for now. Later the object will hide all the logic of retrieving properties like:
  * - load from .conf or .properties file
  * - select from database
  * - api call
  * - etc...
  */

object TestConfig {

  val baseUrl = "https://demo.yggdrasilgaming.com/game.web"
  val exitCondition = "$.data.wager.bets[0].wonamount"

}
