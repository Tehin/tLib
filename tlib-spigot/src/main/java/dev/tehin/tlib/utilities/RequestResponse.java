package dev.tehin.tlib.utilities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RequestResponse {

    private final boolean approved;
    private final String reason;

    public static RequestResponse approve() {
        return new RequestResponse(true, "");
    }

    public static RequestResponse decline(String reason) {
        return new RequestResponse(false, reason);
    }

}
