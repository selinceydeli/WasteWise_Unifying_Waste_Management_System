package wastewise.domain;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AddressValidationService {

    // TODO
    // @Value("${google.maps.api.key}")
    private static String apiKey;

    private final transient RestTemplate restTemplate;

    public AddressValidationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Validates a given address.
     *
     * @param address Address to validate.
     * @return Response of Maps API.
     */
    public String validateAddress(String address) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json";

        String response = restTemplate.getForObject(
                UriComponentsBuilder.fromHttpUrl(url)
                        .queryParam("address", address)
                        .queryParam("key", apiKey)
                        .toUriString(),
                String.class);

        return response; // You can parse and return specific parts of the response
    }

}
