package bots

import com.vdurmont.emoji.EmojiParser
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendDocument

import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

class WCCBot : TelegramLongPollingBot() {

    override fun getBotUsername(): String {
        //return bot username
        // If bot username is @HelloKotlinBot, it must return
        return "HelloKotlinBot"
    }

    override fun getBotToken(): String {
        // Return bot token from BotFather
        return "993730170:AAHtSH0dKYxUWRNcG3ZgG5hE4WEi8PmL5Zg"
    }

    override fun onUpdateReceived(update: Update?) {
        // We check if the update has a message and the message has text
        val nameSender = update?.message?.from?.firstName
        val chatId = update?.message?.chatId.toString()
        val messageCommand = update?.message?.text?.lowercase()

        try {

            if( messageCommand == "/music"){
                val sendMusic = SendAudio()

            } else {

                val sendDocument = SendDocument().apply {
                    this.chatId = chatId
                    this.caption = getMessage(messageCommand, nameSender)
                    this.document = InputFile().setMedia("https://media.giphy.com/media/zgSWpnMeK7dCM/giphy.gif")
                    this.parseMode = "MarkdownV2"
                }
                execute(sendDocument)
            }
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    private fun getMessage(command: String?, nameSender: String?) = when(command) {
        "/info" -> EmojiParser.parseToUnicode("Nao há informaçoes :weary:")
        "/start" -> welcome(nameSender)
        else -> EmojiParser.parseToUnicode("num xei :weary:")
    }

    private fun welcome(nameSender: String?) = """
        |*Oláaaa $nameSender, tudo bemm\?*
        |
        |\/start \- começar o projeto
        |\/info \- para saber mais sobre o projeto
        |\/seila \- blablabla
    """.trimMargin()

}

