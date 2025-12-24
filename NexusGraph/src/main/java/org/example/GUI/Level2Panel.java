package org.example.GUI;

import org.example.Level_2.*;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Level2Panel extends VBox {
    private BooleanProperty xmlValidProperty = new SimpleBooleanProperty(false);
    private BooleanProperty graphBuiltProperty = new SimpleBooleanProperty(false);
    private TextArea resultsArea;
    private TextField mutualIdsField;
    private TextField suggestUserIdField;
    private TextField searchField;
    private ComboBox<String> searchTypeCombo;
    private Button showGraphBtn;
    private Label statusLabel;
    private Runnable onShowGraph;
    private Runnable onBuildComplete; // Called after successful build to switch view
    private java.util.function.Supplier<String> xmlContentSupplier;
    private java.util.function.Consumer<String> centerResultsUpdater; // Updates center results area
    private String xmlContent = "";
    private Graph builtGraph;
    private static final String PANEL_BG = "#EEF1F6";
    private static final String ACCENT = "#2563EB";
    private static final String ACCENT_DARK = "#1D4ED8";
    private static final String BUTTON_BG = "#E2E8F0";
    private static final String BUTTON_HOVER = "#CBD5E1";
    private static final String TEXT_MUTED = "#64748B";
    private static final String BORDER = "#D1D5DB";
    private static final String SUCCESS = "#10B981";

    public Level2Panel(Stage stage) {
        this.setSpacing(0);
        this.setPadding(new Insets(0));
        this.setStyle("-fx-background-color: " + PANEL_BG + ";");
        this.setPrefWidth(280);
        this.setMinWidth(250);
        Label header = new Label("Level 2: Graph Analysis");
        header.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + ACCENT + "; " +
                       "-fx-padding: 12 12 8 12;");
        header.setMaxWidth(Double.MAX_VALUE);
        VBox controlsContainer = new VBox(0);
        controlsContainer.setStyle("-fx-background-color: " + PANEL_BG + ";");
        TitledPane graphPane = createGraphControlsSection();
        TitledPane networkPane = createNetworkAnalysisSection();
        TitledPane searchPane = createPostSearchSection();
        Accordion controlsAccordion = new Accordion();
        controlsAccordion.getPanes().addAll(graphPane, networkPane, searchPane);
        controlsAccordion.setExpandedPane(graphPane);
        
        controlsContainer.getChildren().add(controlsAccordion);
        ScrollPane controlsScroll = new ScrollPane(controlsContainer);
        controlsScroll.setFitToWidth(true);
        controlsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        controlsScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(controlsScroll, Priority.SOMETIMES);
        VBox resultsSection = createStaticResultsSection();
        VBox.setVgrow(resultsSection, Priority.ALWAYS);
        
        this.getChildren().addAll(header, new Separator(), controlsScroll, new Separator(), resultsSection);
    }
    
    
    private TitledPane createGraphControlsSection() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(12));
        content.setStyle("-fx-background-color: white;");
        Button buildGraphBtn = new Button("Build Network Graph");
        buildGraphBtn.setMaxWidth(Double.MAX_VALUE);
        buildGraphBtn.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; " +
                              "-fx-background-color: " + ACCENT + "; -fx-text-fill: white; " +
                              "-fx-background-radius: 6; -fx-padding: 10 16; -fx-cursor: hand;");
        buildGraphBtn.setOnMouseEntered(e -> buildGraphBtn.setStyle(buildGraphBtn.getStyle().replace(ACCENT, ACCENT_DARK)));
        buildGraphBtn.setOnMouseExited(e -> buildGraphBtn.setStyle(buildGraphBtn.getStyle().replace(ACCENT_DARK, ACCENT)));
        buildGraphBtn.setOnAction(e -> handleBuildGraph());
        showGraphBtn = new Button("Show Network Graph");
        showGraphBtn.setMaxWidth(Double.MAX_VALUE);
        showGraphBtn.setDisable(true);
        updateShowButtonStyle();
        showGraphBtn.setOnAction(e -> {
            if (onShowGraph != null) {
                onShowGraph.run();
            }
        });
        
        graphBuiltProperty.addListener((obs, oldVal, newVal) -> {
            showGraphBtn.setDisable(!newVal);
            updateShowButtonStyle();
        });
        statusLabel = new Label("Build the graph before displaying it");
        statusLabel.setStyle("-fx-text-fill: " + TEXT_MUTED + "; -fx-font-size: 11px;");
        statusLabel.setWrapText(true);
        
        Separator sep = new Separator();
        
        Label hintLabel = new Label("1. Load and validate XML first\n2. Build the graph\n3. Then show the visualization");
        hintLabel.setStyle("-fx-text-fill: " + TEXT_MUTED + "; -fx-font-size: 10px;");
        hintLabel.setWrapText(true);
        
        content.getChildren().addAll(buildGraphBtn, showGraphBtn, sep, statusLabel, hintLabel);
        
        TitledPane pane = new TitledPane("Graph Controls", content);
        pane.setStyle("-fx-font-weight: bold;");
        pane.setAnimated(false); // Disable animation
        return pane;
    }
    
    private void updateShowButtonStyle() {
        if (showGraphBtn.isDisabled()) {
            showGraphBtn.setStyle("-fx-font-size: 13px; " +
                                 "-fx-background-color: #D1D5DB; -fx-text-fill: #9CA3AF; " +
                                 "-fx-background-radius: 6; -fx-padding: 10 16;");
        } else {
            showGraphBtn.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; " +
                                 "-fx-background-color: " + ACCENT_DARK + "; -fx-text-fill: white; " +
                                 "-fx-background-radius: 6; -fx-padding: 10 16; -fx-cursor: hand;");
        }
    }
    
    
    private void handleBuildGraph() {
        resultsArea.setText("Building network graph...\n\n");
        scrollResultsToTop();
        if ((xmlContent == null || xmlContent.trim().isEmpty()) && xmlContentSupplier != null) {
            xmlContent = xmlContentSupplier.get();
        }
        
        if (xmlContent == null || xmlContent.trim().isEmpty()) {
            resultsArea.appendText("Error: No XML content loaded.\n");
            resultsArea.appendText("Please load an XML file first.");
            statusLabel.setText("Error: No XML content");
            statusLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 11px;");
            return;
        }
        
        try {
            resultsArea.appendText("Parsing XML...\n");
            SocialNetworkLoader loader = new SocialNetworkLoader();
            List<User> users = loader.loadFromString(xmlContent);
            resultsArea.appendText("Found " + users.size() + " users\n\n");
            
            if (users.isEmpty()) {
                resultsArea.appendText("Error: No users found in XML.\n");
                resultsArea.appendText("Make sure XML contains <user> elements.");
                return;
            }
            resultsArea.appendText("Parsed user data:\n");
            int totalFollowers = 0;
            for (User u : users) {
                List<Integer> followers = u.getFollowerIds();
                int followerCount = (followers != null) ? followers.size() : 0;
                totalFollowers += followerCount;
                resultsArea.appendText("  User " + u.getId() + " (" + u.getName() + "): " + 
                                      followerCount + " followers");
                if (followers != null && !followers.isEmpty()) {
                    resultsArea.appendText(" -> " + followers.toString());
                }
                resultsArea.appendText("\n");
            }
            resultsArea.appendText("Total follower entries: " + totalFollowers + "\n\n");
            
            resultsArea.appendText("Building graph structure...\n");
            GraphBuilder builder = new GraphBuilder();
            builtGraph = builder.buildGraph(users);
            ArrayList<Pair<String, String>> edges = builtGraph.getEdges();
            resultsArea.appendText("Created " + edges.size() + " connections\n");
            if (!edges.isEmpty()) {
                resultsArea.appendText("Edges:\n");
                for (Pair<String, String> edge : edges) {
                    resultsArea.appendText("  " + edge.toString() + "\n");
                }
            }
            resultsArea.appendText("\n");
            
            resultsArea.appendText("Generating graph image...\n");
            Graph2Photo photoGenerator = new Graph2Photo(edges);
            photoGenerator.Graph2Photoprint("png");
            resultsArea.appendText("Output: Graphs/graph.png\n");
            resultsArea.appendText("Output: Graphs/graph.dot\n\n");
            
            resultsArea.appendText("Graph built successfully.\n\n");
            resultsArea.appendText("Click 'Show Network Graph' to view the visualization.");
            
            graphBuiltProperty.set(true);
            statusLabel.setText("Graph ready - Click 'Show' to view");
            statusLabel.setStyle("-fx-text-fill: " + SUCCESS + "; -fx-font-size: 11px;");
            if (onBuildComplete != null) {
                onBuildComplete.run();
            }
            
        } catch (Exception e) {
            resultsArea.appendText("Error: " + e.getMessage() + "\n");
            e.printStackTrace();
            statusLabel.setText("Build failed");
            statusLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 11px;");
        }
        scrollResultsToTop();
    }
    
    
    private TitledPane createNetworkAnalysisSection() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(12));
        content.setStyle("-fx-background-color: white;");
        
        Button mostActiveBtn = createSecondaryButton("Most Active User");
        mostActiveBtn.disableProperty().bind(graphBuiltProperty.not());
        mostActiveBtn.setOnAction(e -> handleMostActive());
        
        Button mostInfluencerBtn = createSecondaryButton("Most Influencer");
        mostInfluencerBtn.disableProperty().bind(graphBuiltProperty.not());
        mostInfluencerBtn.setOnAction(e -> handleMostInfluencer());
        
        Separator sep1 = new Separator();
        
        Label mutualLabel = new Label("Mutual Followers (comma-separated IDs):");
        mutualLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED + "; -fx-font-weight: normal;");
        
        mutualIdsField = new TextField();
        mutualIdsField.setPromptText("e.g., 1, 2, 3");
        mutualIdsField.setStyle("-fx-background-radius: 4; -fx-border-radius: 4; -fx-border-color: " + BORDER + ";");
        
        Button mutualBtn = createSecondaryButton("Find Mutual Followers");
        mutualBtn.disableProperty().bind(graphBuiltProperty.not());
        mutualBtn.setOnAction(e -> handleMutualFollowers());
        
        Separator sep2 = new Separator();
        
        Label suggestLabel = new Label("Suggest Users for ID:");
        suggestLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED + "; -fx-font-weight: normal;");
        
        suggestUserIdField = new TextField();
        suggestUserIdField.setPromptText("User ID");
        suggestUserIdField.setStyle("-fx-background-radius: 4; -fx-border-radius: 4; -fx-border-color: " + BORDER + ";");
        
        Button suggestBtn = createSecondaryButton("Get Suggestions");
        suggestBtn.disableProperty().bind(graphBuiltProperty.not());
        suggestBtn.setOnAction(e -> handleSuggestUsers());
        
        content.getChildren().addAll(
            mostActiveBtn, mostInfluencerBtn,
            sep1, mutualLabel, mutualIdsField, mutualBtn,
            sep2, suggestLabel, suggestUserIdField, suggestBtn
        );
        
        TitledPane pane = new TitledPane("Network Analysis", content);
        pane.setStyle("-fx-font-weight: bold;");
        pane.setExpanded(false);
        pane.setAnimated(false); // Disable animation
        return pane;
    }
    
    private void handleMostActive() {
        if (builtGraph == null) {
            updateCenterResults("Error: Graph not built. Please build the graph first.");
            return;
        }
        try {
            NetworkAnalyzer_2 analyzer = new NetworkAnalyzer_2(builtGraph);
            List<Integer> activeIds = analyzer.mostActiveUsers();
            
            StringBuilder result = new StringBuilder();
            if (activeIds.isEmpty()) {
                result.append("Most Active Users:\n\nNo active users found.");
            } else {
                int[][] matrix = builtGraph.getAdjacencyMatrix();
                int maxFollowing = 0;
                for (int idx : activeIds) {
                    int count = 0;
                    for (int j = 0; j < matrix[idx].length; j++) {
                        if (matrix[idx][j] == 1) count++;
                    }
                    maxFollowing = Math.max(maxFollowing, count);
                }
                
                result.append("Most Active User(s) following ").append(maxFollowing).append(" people:\n\n");
                List<Vertex> vertices = builtGraph.getVertices();
                for (int idx : activeIds) {
                    if (idx >= 0 && idx < vertices.size()) {
                        User user = vertices.get(idx).getUser();
                        result.append("- ").append(user.getName()).append(" (ID: ").append(user.getId()).append(")\n");
                    }
                }
            }
            updateCenterResults(result.toString());
        } catch (Exception e) {
            updateCenterResults("Error: " + e.getMessage());
        }
    }
    
    private void handleMostInfluencer() {
        if (builtGraph == null) {
            updateCenterResults("Error: Graph not built. Please build the graph first.");
            return;
        }
        try {
            NetworkAnalyzer_2 analyzer = new NetworkAnalyzer_2(builtGraph);
            String result = analyzer.mostInfluencerUser();
            updateCenterResults(result);
        } catch (Exception e) {
            updateCenterResults("Error: " + e.getMessage());
        }
    }
    
    private void handleMutualFollowers() {
        if (builtGraph == null) {
            updateCenterResults("Error: Graph not built. Please build the graph first.");
            return;
        }
        String idsText = mutualIdsField.getText().trim();
        if (idsText.isEmpty()) {
            updateCenterResults("Please enter comma-separated user IDs.");
            return;
        }
        try {
            String[] parts = idsText.split(",");
            List<Integer> ids = new ArrayList<>();
            for (String p : parts) {
                ids.add(Integer.parseInt(p.trim()));
            }
            NetworkAnalyzer_2 analyzer = new NetworkAnalyzer_2(builtGraph);
            String result = analyzer.mutualFollowers(ids);
            updateCenterResults("Mutual Followers for IDs: " + idsText + "\n\n" + result);
        } catch (NumberFormatException e) {
            updateCenterResults("Error: Invalid ID format. Please enter numbers separated by commas.");
        } catch (Exception e) {
            updateCenterResults("Error: " + e.getMessage());
        }
    }
    
    private void handleSuggestUsers() {
        if (builtGraph == null) {
            updateCenterResults("Error: Graph not built. Please build the graph first.");
            return;
        }
        String idText = suggestUserIdField.getText().trim();
        if (idText.isEmpty()) {
            updateCenterResults("Please enter a user ID.");
            return;
        }
        try {
            int userId = Integer.parseInt(idText);
            NetworkAnalyzer_2 analyzer = new NetworkAnalyzer_2(builtGraph);
            List<Integer> suggestions = analyzer.suggestUsers(userId);
            StringBuilder result = new StringBuilder();
            result.append("Suggested Users for ID ").append(userId).append(":\n\n");
            if (suggestions.isEmpty()) {
                result.append("No suggestions found.");
            } else {
                List<Vertex> vertices = builtGraph.getVertices();
                for (int idx : suggestions) {
                    if (idx >= 0 && idx < vertices.size()) {
                        User user = vertices.get(idx).getUser();
                        result.append("- ").append(user.getName()).append(" (ID: ").append(user.getId()).append(")\n");
                    }
                }
            }
            updateCenterResults(result.toString());
        } catch (Exception e) {
            updateCenterResults("Error: " + e.getMessage());
        }
    }
    
    
    private TitledPane createPostSearchSection() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(12));
        content.setStyle("-fx-background-color: white;");
        
        searchTypeCombo = new ComboBox<>();
        searchTypeCombo.getItems().addAll("Search by Word", "Search by Topic");
        searchTypeCombo.setValue("Search by Word");
        searchTypeCombo.setMaxWidth(Double.MAX_VALUE);
        searchTypeCombo.setStyle("-fx-background-radius: 4;");
        
        searchField = new TextField();
        searchField.setPromptText("Enter keyword or topic...");
        searchField.setStyle("-fx-background-radius: 4; -fx-border-radius: 4; -fx-border-color: " + BORDER + ";");
        
        Button searchBtn = new Button("Search Posts");
        searchBtn.setMaxWidth(Double.MAX_VALUE);
        searchBtn.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; " +
                          "-fx-background-color: " + ACCENT + "; -fx-text-fill: white; " +
                          "-fx-background-radius: 6; -fx-padding: 8 12; -fx-cursor: hand;");
        searchBtn.disableProperty().bind(graphBuiltProperty.not());
        searchBtn.setOnAction(e -> handleSearch());
        
        content.getChildren().addAll(searchTypeCombo, searchField, searchBtn);
        
        TitledPane pane = new TitledPane("Post Search", content);
        pane.setStyle("-fx-font-weight: bold;");
        pane.setExpanded(false);
        pane.setAnimated(false); // Disable animation
        return pane;
    }
    
    private void handleSearch() {
        String query = searchField.getText().trim();
        String type = searchTypeCombo.getValue();
        
        if (query.isEmpty()) {
            resultsArea.setText("Please enter a search term.");
            scrollResultsToTop();
            return;
        }
        
        if (xmlContent == null || xmlContent.isEmpty()) {
            updateCenterResults("No XML content to search.");
            return;
        }
        
        try {
            SocialNetworkLoader loader = new SocialNetworkLoader();
            List<User> users = loader.loadFromString(xmlContent);
            
            boolean byWord = type.contains("Word");
            StringBuilder result = new StringBuilder();
            result.append((byWord ? "Word" : "Topic") + " Search: \"" + query + "\"\n\n");
            
            boolean found = false;
            for (User user : users) {
                List<Post> posts = user.getPosts();
                if (posts != null) {
                    for (Post post : posts) {
                        boolean matches = false;
                        
                        if (byWord) {
                            String text = post.getText();
                            if (text != null && text.toLowerCase().contains(query.toLowerCase())) {
                                matches = true;
                            }
                        } else {
                            List<String> topics = post.getTopics();
                            if (topics != null) {
                                for (String t : topics) {
                                    if (t.toLowerCase().contains(query.toLowerCase())) {
                                        matches = true;
                                        break;
                                    }
                                }
                            }
                        }
                        
                        if (matches) {
                            result.append("User: ").append(user.getName()).append(" (ID: ").append(user.getId()).append(")\n");
                            result.append("Post: ").append(post.getText()).append("\n\n");
                            found = true;
                        }
                    }
                }
            }
            
            if (!found) {
                result.append("No posts found matching: ").append(query);
            }
            updateCenterResults(result.toString());
        } catch (Exception e) {
            updateCenterResults("Error: " + e.getMessage());
        }
    }
    
    
    private VBox createStaticResultsSection() {
        VBox section = new VBox(12);
        section.setPadding(new Insets(16));
        section.setAlignment(Pos.CENTER);
        section.setStyle("-fx-background-color: white;");
        Label headerLabel = new Label("Results");
        headerLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        resultsArea = new TextArea();
        resultsArea.setVisible(false);
        resultsArea.setManaged(false);
        Button showResultsBtn = new Button("Show Results");
        showResultsBtn.setMaxWidth(Double.MAX_VALUE);
        showResultsBtn.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                               "-fx-background-color: " + ACCENT + "; -fx-text-fill: white; " +
                               "-fx-background-radius: 6; -fx-padding: 12 24; -fx-cursor: hand;");
        showResultsBtn.setOnMouseEntered(e -> showResultsBtn.setStyle(showResultsBtn.getStyle().replace(ACCENT, ACCENT_DARK)));
        showResultsBtn.setOnMouseExited(e -> showResultsBtn.setStyle(showResultsBtn.getStyle().replace(ACCENT_DARK, ACCENT)));
        showResultsBtn.setOnAction(e -> {
            if (onBuildComplete != null) {
                onBuildComplete.run();
            }
        });
        Label infoLabel = new Label("Click to view analysis results");
        infoLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED + ";");
        
        section.getChildren().addAll(headerLabel, showResultsBtn, infoLabel, resultsArea);
        
        return section;
    }
    
    
    private void scrollResultsToTop() {
        resultsArea.setScrollTop(0);
        resultsArea.positionCaret(0);
    }
    
    private Button createSecondaryButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-font-size: 12px; " +
                    "-fx-background-color: " + BUTTON_BG + "; " +
                    "-fx-text-fill: #374151; " +
                    "-fx-background-radius: 6; " +
                    "-fx-padding: 8 12; " +
                    "-fx-cursor: hand;");
        btn.setOnMouseEntered(e -> {
            if (!btn.isDisabled()) btn.setStyle(btn.getStyle().replace(BUTTON_BG, BUTTON_HOVER));
        });
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace(BUTTON_HOVER, BUTTON_BG)));
        return btn;
    }
    
    public void setXmlValid(boolean valid) {
        xmlValidProperty.set(valid);
        if (!valid) {
            graphBuiltProperty.set(false);
            builtGraph = null;
        }
    }
    
    public void setXmlContent(String content) {
        this.xmlContent = content;
    }
    
    public void setOnShowGraph(Runnable callback) {
        this.onShowGraph = callback;
    }
    
    
    public void setOnBuildComplete(Runnable callback) {
        this.onBuildComplete = callback;
    }
    
    
    public void setXmlContentSupplier(java.util.function.Supplier<String> supplier) {
        this.xmlContentSupplier = supplier;
    }
    
    
    public void setCenterResultsUpdater(java.util.function.Consumer<String> updater) {
        this.centerResultsUpdater = updater;
    }
    
    
    private void updateCenterResults(String text) {
        if (centerResultsUpdater != null) {
            centerResultsUpdater.accept(text);
        }
        resultsArea.setText(text);
        scrollResultsToTop();
    }
    
    public BooleanProperty xmlValidProperty() {
        return xmlValidProperty;
    }
    
    public BooleanProperty graphBuiltProperty() {
        return graphBuiltProperty;
    }
    
    public void setResults(String text) {
        resultsArea.setText(text);
        scrollResultsToTop();
    }
    
    public void appendResults(String text) {
        resultsArea.appendText(text);
    }
    
    
    public String getResultsText() {
        return resultsArea.getText();
    }
}
