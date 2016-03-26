package jerseywiremock.core.stub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;

public class GetSingleRequestStubber<Entity>
        extends EmptyRequestSimpleResponseRequestStubber<Entity, GetSingleResponseStubber<Entity>>
{
    public GetSingleRequestStubber(
            WireMockServer wireMockServer,
            ObjectMapper objectMapper,
            MappingBuilder mappingBuilder
    ) {
        super(wireMockServer, objectMapper, mappingBuilder);
    }

    @Override
    public GetSingleResponseStubber<Entity> andRespond() {
        return new GetSingleResponseStubber<>(wireMockServer, objectMapper, mappingBuilder);
    }
}
