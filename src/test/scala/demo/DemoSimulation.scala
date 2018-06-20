package demo

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._


class DemoSimulation extends Simulation{

  private val gameId = 7316

  private val httpConf = http
    .baseURL(TestConfig.baseUrl)
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  private val getSessionId = exec(
    http("getSessionId")
      .get("/service")
      .queryParam("fn", "authenticate")
      .queryParam("org","Demo")
      .queryParam("gameid", gameId)
      .check(jsonPath("$.data.sessid").saveAs("sessid")
      )
  )

  private val runGame = exec(
    http("runGame")
      .get("/service")
      .queryParam("fn", "play")
      .queryParam("gameid", gameId)
      .queryParam("sessid", "${sessid}")
      .queryParam("currency", "EUR")
      .queryParam("coin", "0.2")
      .queryParam("amount", "5")
      .check(jsonPath(TestConfig.exitCondition).saveAs("endCondition")))
    .pause(4)


  val demoScenario: ScenarioBuilder = scenario("DemoSimulation")
    .exec(
      getSessionId,
      asLongAs(session => session.get("endCondition").asOption[String].getOrElse("0.00") == "0.00"){runGame}
    )

  setUp(
    demoScenario.inject(atOnceUsers(1))
  ).protocols(httpConf).maxDuration(60)

}
