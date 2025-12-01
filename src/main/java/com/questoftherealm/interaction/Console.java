package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.InputService;
import com.questoftherealm.game.interfaces.Output;

import java.io.IOException;

import static com.questoftherealm.game.GameConstants.RED;
import static com.questoftherealm.game.GameConstants.RESET;


public class Console {
    private final GameState state;
    private final Output output;
    private final InputService input;
    private final SlowPrinter slowPrinter;

    public Console(GameState state) {
        this.state = state;
        this.output = state.getGameServices().getOutput();
        this.input = state.getGameServices().getInput();
        this.slowPrinter = new SlowPrinter(state);
    }

    public void displayTitle() {
        output.println();
        output.println();
        output.println();
        output.println();
        output.println("""
                                                              ░██████   ░██     ░██ ░██████████   ░██████   ░██████████     ░██████   ░██████████   ░██████████░██     ░██ ░██████████ 
                                                             ░██   ░██  ░██     ░██ ░██          ░██   ░██      ░██        ░██   ░██  ░██               ░██    ░██     ░██ ░██         
                                                            ░██     ░██ ░██     ░██ ░██         ░██             ░██       ░██     ░██ ░██               ░██    ░██     ░██ ░██         
                                                            ░██     ░██ ░██     ░██ ░█████████   ░████████      ░██       ░██     ░██ ░█████████        ░██    ░██████████ ░█████████  
                                                            ░██     ░██ ░██     ░██ ░██                 ░██     ░██       ░██     ░██ ░██               ░██    ░██     ░██ ░██         
                                                             ░██   ░██   ░██   ░██  ░██          ░██   ░██      ░██        ░██   ░██  ░██               ░██    ░██     ░██ ░██         
                                                              ░██████     ░██████   ░██████████   ░██████       ░██         ░██████   ░██               ░██    ░██     ░██ ░██████████
                                                                   ░██                                                                                                                
                                                                    ░██                                                                                                               
                                                                                                                                                                        
                                                                                  ░█████████  ░██████████    ░███    ░██         ░███     ░███   ░██████                                        
                                                                                  ░██     ░██ ░██           ░██░██   ░██         ░████   ░████  ░██   ░██                                       
                                                                                  ░██     ░██ ░██          ░██  ░██  ░██         ░██░██ ░██░██ ░██                                              
                                                                                  ░█████████  ░█████████  ░█████████ ░██         ░██ ░████ ░██  ░████████                                       
                                                                                  ░██   ░██   ░██         ░██    ░██ ░██         ░██  ░██  ░██         ░██                                      
                                                                                  ░██    ░██  ░██         ░██    ░██ ░██         ░██       ░██  ░██   ░██                                       
                                                                                  ░██     ░██ ░██████████ ░██    ░██ ░██████████ ░██       ░██   ░██████                                        
                                                                                                                                          
                                                                                                                                          
                                                                                                                                                     
                """);
    }

    public void worldIntro() {
        slowPrinter.slowPrint(state.getMessages().getBundle().get("console.worldIntro"));
        state.getGameServices().getInput().nextLine();
        output.println();
    }

//    public void displayPlayTime(Player player) {
//        int h = player.getPlayTime().hours();
//        int m = player.getPlayTime().minutes();
//
//        String playTimeArt = String.format(state.getMessages().getBundle().get("console.playTimeArt"), h, m);
//        output.println(playTimeArt);
//    }
    public void displayPlayTime(Player player) {
        int h = player.getPlayTime().hours();
        int m = player.getPlayTime().minutes();

        String playTimeArt = String.format("""
                                                                                                    ┏━┓╻  ┏━┓╻ ╻   ╺┳╸╻┏┳┓┏━╸   \s
                                                                                                    ┣━┛┃  ┣━┫┗┳┛    ┃ ┃┃┃┃┣╸  ╺━╸  %02d hours %02d minutes
                                                                                                    ╹  ┗━╸╹ ╹ ╹     ╹ ╹╹ ╹┗━╸
                """, h, m);
        output.println(playTimeArt);
    }


    public void displayEnd(Player player) {
        output.println();
        output.println();
        output.println();
        output.println();
        output.println("""
                                                                █████████    █████████   ██████   ██████ ██████████       ███████    █████   █████ ██████████ ███████████ 
                                                               ███░░░░░███  ███░░░░░███ ░░██████ ██████ ░░███░░░░░█     ███░░░░░███ ░░███   ░░███ ░░███░░░░░█░░███░░░░░███
                                                              ███     ░░░  ░███    ░███  ░███░█████░███  ░███  █ ░     ███     ░░███ ░███    ░███  ░███  █ ░  ░███    ░███
                                                             ░███          ░███████████  ░███░░███ ░███  ░██████      ░███      ░███ ░███    ░███  ░██████    ░██████████ 
                                                             ░███    █████ ░███░░░░░███  ░███ ░░░  ░███  ░███░░█      ░███      ░███ ░░███   ███   ░███░░█    ░███░░░░░███
                                                             ░░███  ░░███  ░███    ░███  ░███      ░███  ░███ ░   █   ░░███     ███   ░░░█████░    ░███ ░   █ ░███    ░███
                                                              ░░█████████  █████   █████ █████     █████ ██████████    ░░░███████░      ░░███      ██████████ █████   █████
                                                               ░░░░░░░░░  ░░░░░   ░░░░░ ░░░░░     ░░░░░ ░░░░░░░░░░       ░░░░░░░         ░░░      ░░░░░░░░░░ ░░░░░   ░░░░░
                                                                                                                                                                          
                                                                                                                                                                          
                                                                                                                                                                          
                """);

        displayPlayTime(player);
    }

    public void showIntro() throws IOException {
        Story story = new Story();
        final int delay = 30;
        int count = 0;
        output.println();
        output.println(state.getMessages().getBundle().get("console.intro.skip", RED, RESET));

        for (char c : story.getStory(state).toCharArray()) {
            output.print(String.valueOf(c));
            count++;
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if (input.available() > 0) {
                while (input.available() > 0) {
                    input.read();
                }
                output.print(story.getStory(state).substring(count));
                break;
            }
        }
        input.read();
    }

    public int showMainMenu(int count, GameState state) {
        if (count <= 1) {
            output.println(
                    """
                            ╺┓     ┏┓╻┏━╸╻ ╻   ┏━╸┏━┓┏┳┓┏━╸  
                             ┃     ┃┗┫┣╸ ┃╻┃   ┃╺┓┣━┫┃┃┃┣╸   
                            ╺┻╸╹   ╹ ╹┗━╸┗┻┛   ┗━┛╹ ╹╹ ╹┗━╸  
                            ┏━┓    ╻  ┏━┓┏━┓╺┳┓   ┏━╸┏━┓┏┳┓┏━╸
                            ┏━┛    ┃  ┃ ┃┣━┫ ┃┃   ┃╺┓┣━┫┃┃┃┣╸
                            ┗━╸╹   ┗━╸┗━┛╹ ╹╺┻┛   ┗━┛╹ ╹╹ ╹┗━╸
                                                             
                            """);
            output.print(state.getMessages().getBundle().get("console.menu.prompt"));
        } else {
            output.print(state.getMessages().getBundle().get("console.menu.prompt"));
        }
        return Integer.parseInt(state.getGameServices().getInput().nextLine());
    }

    public String characterCreationScreen(GameState state) {
        output.println();
        output.println(state.getMessages().getBundle().get("console.charCreate.namePrompt"));
        output.print(state.getMessages().getBundle().get("console.menu.prompt"));
        String name = state.getGameServices().getInput().nextLine();
        int count = 0;
        while (name.isBlank()) {
            if (count < 1) {
                output.println(state.getMessages().getBundle().get("console.charCreate.nameEmptyError"));
            }
            output.print(state.getMessages().getBundle().get("console.menu.prompt"));
            name = state.getGameServices().getInput().nextLine();
            count++;
        }
        output.println(state.getMessages().getBundle().get("console.charCreate.classPrompt"));
        output.println(state.getMessages().getBundle().get("console.charCreate.class1"));
        output.println(state.getMessages().getBundle().get("console.charCreate.class2"));
        output.println(state.getMessages().getBundle().get("console.charCreate.class3"));
        output.println(state.getMessages().getBundle().get("console.charCreate.class4"));
        return name;
    }

    private void displayModes() {
    }
}