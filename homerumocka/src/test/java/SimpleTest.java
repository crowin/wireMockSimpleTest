import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Result;

/**
 * Created by Alexander on 08.07.2017.
 */
public class SimpleTest {

    //junit rule. Service create before starting each test and down when test finished
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    //parsing all request for listening address
    @Test
    public void FirthTest() {
        stubFor(post(urlEqualTo("/my/resource"))
                //.withHeader("Accept", equalTo("text/xml"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>")
                ));

        //rest-assured create post request for testing mock service and send request on mock service
        Response response = RestAssured.given().
                header(new Header("Content-Type", "application/json")).
                body("{name: 'test'}")
                .post("http://localhost:8089/my/resource");
        response.statusLine();
        System.out.println(response.headers().toString());
        response.body().prettyPrint();

        //verify request
        verify(postRequestedFor(urlMatching("/my/resource"))
                .withRequestBody(matching("\\{name: .*\\}"))
                .withHeader("Content-Type", notMatching("application/json")));
    }
}
