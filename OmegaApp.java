package cs1302.omega;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Animation.Status;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.net.URL;
import java.net.URLEncoder;
import java.io.InputStreamReader;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.util.ArrayList;


/**
 * An app that lets a user click on a Pokemon region, then displays a video
 * about it as well as information about a random Pokemon from that region.
 */
public class OmegaApp extends Application {

    // instance variables for various content
    HBox contentHolder;
    Timeline timeline;
    ArrayList<String> imageList;

    // instance variables for instructions
    VBox instructionHolder;
    Label instructions1;
    Label instructions2;
    Label blankInstructions;

    // instance variables for button interface
    VBox buttonHolder;
    Label regionText;
    Button kantoButton;
    Button johtoButton;
    Button hoennButton;
    Button sinnohButton;
    Button unovaButton;
    Button kalosButton;
    Button alolaButton;
    Button galarButton;

    // instance variables for visual content
    VBox visualContent;
    ImageView pokemonImage;
    Image pokeballImage;
    ImageView regionTrailer;
    Image defaultRegionImage;
    ImageView tcgCard;

    // instance variables for text-based content
    VBox textContent;
    Label pokemonName;
    Label pokemonId;
    Label pokemonHeight;
    Label pokemonWeight;
    Label cardText;
    Label errorText;
    Label errorText2;
    Label space1;
    Label space2;
    Label space3;
    Label space4;
    Label space5;
    Label space6;


    /**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public OmegaApp() {}

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        // initializes instance variables for instructions and buttons
        initRegions();
        instructionHolder = new VBox(instructions1, instructions2, blankInstructions);
        buttonHolder = new VBox(regionText, kantoButton, johtoButton, hoennButton, sinnohButton,
        unovaButton, kalosButton, alolaButton, galarButton);
        // initializes instance variables for visual content
        initImageContent();
        visualContent = new VBox(regionTrailer, new Label(""), pokemonImage);
        // initializes instance variables for text-based content
        textContent = new VBox(pokemonName, space1, pokemonId, space2, pokemonHeight,
                               space3, pokemonWeight, space4, tcgCard, cardText,
                               errorText, errorText2, space6);
        // sets up timeline
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        EventHandler<ActionEvent> handler = event -> swapCards(imageList);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(5), handler);
        timeline.getKeyFrames().add(keyFrame);
        // Gives buttons their action events
        kantoButton.setOnAction((ae) -> {
            int randomizedId = (int)Math.floor(Math.random() * 151 + 1);
            randomizePokemon(pokemonName, pokemonId, pokemonImage,
                             randomizedId, pokemonHeight, pokemonWeight, timeline); });
        johtoButton.setOnAction((ae) -> {
            int randomizedId = (int)Math.floor(Math.random() * 100 + 152);
            randomizePokemon(pokemonName, pokemonId, pokemonImage,
                             randomizedId, pokemonHeight, pokemonWeight, timeline); });
        hoennButton.setOnAction((ae) -> {
            int randomizedId = (int)Math.floor(Math.random() * 135 + 252);
            randomizePokemon(pokemonName, pokemonId, pokemonImage,
                             randomizedId, pokemonHeight, pokemonWeight, timeline); });
        sinnohButton.setOnAction((ae) -> {
            int randomizedId = (int)Math.floor(Math.random() * 107 + 387);
            randomizePokemon(pokemonName, pokemonId, pokemonImage,
                             randomizedId, pokemonHeight, pokemonWeight, timeline); });
        unovaButton.setOnAction((ae) -> {
            int randomizedId = (int)Math.floor(Math.random() * 156 + 494);
            randomizePokemon(pokemonName, pokemonId, pokemonImage,
                             randomizedId, pokemonHeight, pokemonWeight, timeline); });
        kalosButton.setOnAction((ae) -> {
            int randomizedId = (int)Math.floor(Math.random() * 72 + 650);
            randomizePokemon(pokemonName, pokemonId, pokemonImage,
                             randomizedId, pokemonHeight, pokemonWeight, timeline); });
        alolaButton.setOnAction((ae) -> {
            int randomizedId = (int)Math.floor(Math.random() * 90 + 722);
            randomizePokemon(pokemonName, pokemonId, pokemonImage,
                             randomizedId, pokemonHeight, pokemonWeight, timeline); });
        galarButton.setOnAction((ae) -> {
            int randomizedId = (int)Math.floor(Math.random() * 89 + 810);
            randomizePokemon(pokemonName, pokemonId, pokemonImage,
                             randomizedId, pokemonHeight, pokemonWeight, timeline); });
        // setup scene
        contentHolder = new HBox(buttonHolder, visualContent, textContent);
        VBox root = new VBox(instructionHolder, contentHolder);
        setMyStage(stage, root);
    } // start


    /**
     * Swaps the pokemon TCG card on display.
     * @param imageList list of the links of all cards associated with the random pokemon
     */
    public void swapCards(ArrayList<String> imageList) {
        // Checks if the Pokemon has a card
        if (imageList.size() == 0) {
            tcgCard.setImage(new Image("file:resources/card-back.jpg"));
        } else {
            // Picks a random card from the list to display
            int randomNum = (int)Math.floor(Math.random() * imageList.size());
            String pickedImageString = imageList.get(randomNum);
            Image pickedCard = new Image(pickedImageString);
            tcgCard.setImage(pickedCard);
        } // if
    } // swapCards


    /**
     * Gets a random Pokemon from the specified region and changes the GUI to fit that Pokemon.
     * @param pokemonName name of Pokemon
     * @param pokemonId ID of Pokemon
     * @param pokemonImage image of Pokemon
     * @param randomizedId random ID from specified region
     * @param pokemonHeight height of Pokemon
     * @param pokemonWeight weight of Pokemon
     * @param timeline changes the tcg image every 5 seconds
     */
    public void randomizePokemon(Label pokemonName, Label pokemonId, ImageView pokemonImage,
                                 int randomizedId, Label pokemonHeight, Label pokemonWeight,
                                 Timeline timeline) {
        if (timeline.getStatus() == Status.RUNNING) {
            timeline.pause();
        } // if
        // Sets the Pokemon ID on the GUI
        String stringId = String.valueOf(randomizedId);
        pokemonId.setText("ID: " + stringId);
        try {
            // Gets Pokemon name from the PokeAPI
            String sURL = new String("https://pokeapi.co/api/v2/pokemon/" + randomizedId);
            URL url = new URL(sURL);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonElement je = JsonParser.parseReader(reader);
            JsonObject root = je.getAsJsonObject();
            JsonElement pokemonNameObject = root.get("name");
            String pokemonNameString = pokemonNameObject.toString();
            String name = pokemonNameString.substring(1, pokemonNameString.length() - 1);
            String capitalizedName = name.substring(0,1).toUpperCase() + name.substring(1);
            pokemonName.setText("Name: " + capitalizedName);
            // Gets Pokemon height from the PokeAPI
            JsonElement pokemonHeightObject = root.get("height");
            String pokemonHeightString = pokemonHeightObject.toString();
            int intHeight = Integer.valueOf(pokemonHeightString);
            double doubleHeight = (double)intHeight / 10;
            pokemonHeight.setText("Height: " + doubleHeight + " m");
            // Gets Pokemon weight from the PokeAPI
            JsonElement pokemonWeightObject = root.get("weight");
            String pokemonWeightString = pokemonWeightObject.toString();
            int intWeight = Integer.valueOf(pokemonWeightString);
            double doubleWeight = (double)intWeight / 10;
            pokemonWeight.setText("Weight: " + doubleWeight + " kg");
            // Gets the Pokemon image from the PokeAPI
            JsonElement results = root.get("sprites");
            JsonObject resultObjects = results.getAsJsonObject();
            JsonElement spriteElement = resultObjects.get("front_default");
            String spriteUrl = spriteElement.toString();
            String correctedUrl = spriteUrl.substring(1, spriteUrl.length() - 1);
            Image sprite = new Image(correctedUrl);
            pokemonImage.setImage(sprite);
            getTcgCard(capitalizedName);
            timeline.play();
            // Checks for errors
        } catch (MalformedURLException mue) {
            System.err.println("There is a problem with the URL.");
        } catch (IOException ioe) {
            System.err.println("There is a problem with the input.");
        } // try
    } // getRandomPokemon


    /**
     * Displays a random TCG card associated with the generated pokemon.
     * Pokemon without a card displays the default back of card image instead.
     * @param capitalizedName name of the random Pokemon
     */
    public void getTcgCard (String capitalizedName) {
        // Creates url for TCG API incluing the API key
        imageList = new ArrayList<String>();
        try {
            String query = "https://api.pokemontcg.io/v2/cards?q=name:" + capitalizedName;
            URL url = new URL(query);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonElement je = JsonParser.parseReader(reader);
            JsonObject root = je.getAsJsonObject();
            JsonArray results = root.getAsJsonArray("data");
            int numResults = results.size();
            // Get images then get small from the results then picks a random card to display
            imageList.clear();
            for (int i = 0; i < numResults; i++) {
                JsonObject result = results.get(i).getAsJsonObject();
                JsonObject images = result.get("images").getAsJsonObject();
                JsonElement smallImage = images.get("small");
                String smallImageString = smallImage.getAsString();
                imageList.add(smallImageString);
            } // for
            swapCards(imageList);
            // Checks for errors
        } catch (MalformedURLException mue) {
            System.err.println("There is a problem with the URL.");
        } catch (IOException ioe) {
            System.err.println("There is a problem with the input.");
        } // try
    } // getTcgCard


    /**
     * Sets the stage and scene for the application and shows it.
     * @param stage stage of the application
     * @param root VBox holding the entire GUI
     */
    public void setMyStage(Stage stage, VBox root) {
        Scene scene = new Scene(root);
        // Sets up the stage
        stage.setTitle("OmegaApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.setMinWidth(600);
        stage.sizeToScene();
        stage.show();
    } // setMyStage


    /**
     * Initializes the region buttons, Pokemon descriptions, and instructions.
     */
    public void initRegions() {
        // Initializes instructions
        instructions1 = new Label("                                        " +
                                  "Click a region to learn about the Pokemon in it!");
        instructions2 = new Label("                                     " +
                                  "You will also see the Pokemon's various trading cards!");
        blankInstructions = new Label("");
        // Initializes buttons
        regionText = new Label("Regions:");
        kantoButton = new Button("Kanto");
        johtoButton = new Button("Johto");
        hoennButton = new Button("Hoenn");
        sinnohButton = new Button("Sinnoh");
        unovaButton = new Button("Unova");
        kalosButton = new Button("Kalos");
        alolaButton = new Button("Alola");
        galarButton = new Button("Galar");
        // Initializes descriptions of the Pokemon
        pokemonName = new Label("Name: ");
        space1 = new Label("");
        pokemonId = new Label("ID: ");
        space2 = new Label("");
        pokemonHeight = new Label("Height: ");
        space3 = new Label("");
        pokemonWeight = new Label("Weight: ");
        space4 = new Label("");
        cardText = new Label("Card of the Pokemon rotates every 5 seconds.");
        space5 = new Label("");
        errorText = new Label("If there isn't a card for the Pokemon,");
        errorText2 = new Label("the pokeball image appears.");
        space6 = new Label("");
    } // initRegions


    /**
     * Initializes the visual content.
     */
    public void initImageContent() {
        // Initializes Pokemon image
        pokeballImage = new Image("file:resources/pokeball.jpg");
        pokemonImage = new ImageView(pokeballImage);
        pokemonImage.setPreserveRatio(true);
        pokemonImage.setFitWidth(350);
        // Initializes region image
        defaultRegionImage = new Image("file:resources/pokedex.png");
        regionTrailer = new ImageView(defaultRegionImage);
        regionTrailer.setPreserveRatio(true);
        regionTrailer.setFitWidth(300);
        // Initializes TCG card image
        Image backOfCard = new Image("file:resources/card-back.jpg");
        tcgCard = new ImageView(backOfCard);
        tcgCard.setPreserveRatio(true);
        tcgCard.setFitWidth(200);

    } // initImageContent


} // OmegaApp
