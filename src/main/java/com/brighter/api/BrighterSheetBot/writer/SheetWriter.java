package com.brighter.api.BrighterSheetBot.writer;

import com.brighter.api.BrighterSheetBot.dto.UserDto;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class SheetWriter implements Writer {

    private static final String APPLICATION_NAME = "Google Sheets Bots";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String SPREADSHEET_ID = "1G57xKqYwJsysEmgfjpF7McN-tZgZky72VMI-E9rz1Ic"; // <-- USE YOUR SPREADSHEET ID HERE

    private static final String SERVICE_ACCOUNT_FILE_PATH = "/botstaticsheet-4b94c9a9f58d.json";
    public static final String BRIGHTERSHEETBOT_BOTSTATICSHEET_IAM_EMAIL = "brightersheetbot@botstaticsheet.iam.gserviceaccount.com";
    public static final String API_KEY = "API_KEY";

    private static int counter;

    public SheetWriter() {
        counter = 1;
    }


    private static ServiceAccountCredentials getServiceAccountCredentials() throws IOException {
        InputStream in = SheetWriter.class.getResourceAsStream(SERVICE_ACCOUNT_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + SERVICE_ACCOUNT_FILE_PATH);
        }

        ServiceAccountCredentials sourceCredentials = ServiceAccountCredentials.fromStream(in);
        sourceCredentials = (ServiceAccountCredentials) sourceCredentials.createScoped(Arrays.asList(SheetsScopes.SPREADSHEETS));
        return sourceCredentials;
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getOAuthCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = SheetWriter.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH))).setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static void updateSheet(UserDto userDto) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getOAuthCredentials(HTTP_TRANSPORT)).setApplicationName(APPLICATION_NAME).build();
        String range = "Sheet1!A" + counter + ":C";
        String valueInputOption = "RAW";
        ValueRange body = new ValueRange().setValues(Arrays.asList(Arrays.asList(userDto.getId(), userDto.getName(), userDto.getCity())));
        UpdateValuesResponse result = service.spreadsheets().values().update(SPREADSHEET_ID, range, body).setValueInputOption(valueInputOption).execute();
        System.out.println(result);
    }

    private static void updateSheet(Map<String, String> input, String spreadSheetId) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(getServiceAccountCredentials());
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer).setApplicationName(APPLICATION_NAME).build();
        String range = "Sheet1!A" + counter + ":C";
        String valueInputOption = "RAW";
        ValueRange body = new ValueRange().setValues(Arrays.asList(new ArrayList<>(input.values())));
        UpdateValuesResponse result = service.spreadsheets().values().update(spreadSheetId, range, body)
                .setKey(API_KEY).setValueInputOption(valueInputOption).execute();
        System.out.println(result);
    }

    @Override
    public void write(UserDto userDto) {
        this.counter++;
        try {
            this.updateSheet(userDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(Map<String, String> input, String spreadSheetId) {
        this.counter++;
        try {
            System.out.println(input);
            this.updateSheet(input, spreadSheetId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
