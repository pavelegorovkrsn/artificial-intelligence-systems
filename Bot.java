import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        FromDigits runDigits = new FromDigits();
        try {
            runDigits.digits();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void sendMsg(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "MyFirstJavaBot";
    }

    @Override
    public String getBotToken() {
        return "5791853146:AAFS1QJnyRROWst2ri1oZMoxEjMRadbKVAg";
    }

    @Override
    public void onUpdateReceived(Update update) {

       /* final List<PhotoSize> photo = update.getMessage().getPhoto();
        String f_id = photo.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst()
                .orElse(null).getFileId();
        if(photo != null){
            System.out.println(f_id);
        }*/


        /*final Document doc = update.getMessage().getDocument();
        if(doc != null){
            final String fileId = doc.getFileId();
            final String fileName = doc.getFileName();

            System.out.println(fileId);
            System.out.println(fileName);
        }*/

        Model model = new Model();
        Message message = update.getMessage();
        if(message != null && message.hasText() ){
            switch (message.getText() ){
                case "/hello":
                    sendMsg(message, "Hello!!!");
                    break;
                case "/help":
                    sendMsg(message, "Чем могу помочь?");
                    break;
                case "/setting":
                        sendMsg(message, "Что будем настраивать?");
                        break;
                case "/photo":

                    break;

                    default:
                     try{
                      sendMsg(message, Weather.getWeather(message.getText() , model));
                    } catch (IOException e) {
                        sendMsg(message, "Такой город не найден");
                    }
            }

        }
    }



    public void setButtons(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/hello"));
        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }
}
