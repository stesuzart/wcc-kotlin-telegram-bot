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
        return "wccAmandabot"
    }

    override fun getBotToken(): String {
        // Return bot token from BotFather
        return "TOKEN"
    }

    override fun onUpdateReceived(update: Update?) {
        // We check if the update has a message and the message has text
        val nameSender = update?.message?.from?.firstName
        val chatId = update?.message?.chatId.toString()
        val messageCommand = update?.message?.text?.lowercase()

        try {

            if( messageCommand == "/start") {
                val sendDocument = SendDocument().apply {
                    this.chatId = chatId
                    this.caption = getMessage(messageCommand, nameSender)
                    this.document = InputFile().setMedia("https://media.giphy.com/media/3bc9YL28QWi3pYzi1p/giphy.gif")
                    this.parseMode = "MarkdownV2"

                }
                execute(sendDocument)
            }

            else if( messageCommand == "/ouvir") {
                val sendMusic = SendAudio().apply {
                    this.chatId = chatId
                    this.caption = getMessage(messageCommand, nameSender)
                    this.audio = InputFile().setMedia(java.io.File("src/main/resources/1minuto.mp3"))
                    this.title =  "1 minuto - Jean Tassy e Marina Sena"
                    this.parseMode = "MarkdownV2"
                }

                execute(sendMusic)
            }

            else if( messageCommand == "/info") {
                val sendDocument = SendDocument().apply {
                    this.chatId = chatId
                    this.caption = getMessage(messageCommand, nameSender)
                    this.document = InputFile().setMedia("https://media.giphy.com/media/zgSWpnMeK7dCM/giphy.gif")
                    this.parseMode = "MarkdownV2"
                }

                execute(sendDocument)
            }

            else {

                val sendDocument = SendDocument().apply {
                    this.chatId = chatId
                    this.caption = getMessage(messageCommand, nameSender)
                    this.document = InputFile().setMedia("https://media.giphy.com/media/ma7VlDSlty3EA/giphy.gif")
                    this.parseMode = "MarkdownV2"
                }
                execute(sendDocument)
            }
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    private fun getMessage(command: String?, nameSender: String?) = when(command) {
        "/info" -> EmojiParser.parseToUnicode("Para ouvir a música digite /ouvir")
        "/start" -> welcome(nameSender)
        "/ouvir" -> EmojiParser.parseToUnicode("Gostou\\? Compartilhe\\!")
        else -> EmojiParser.parseToUnicode("Não sei o que é isso\\!\nDigite um comando válido")
    }

    private fun welcome(nameSender: String?) = """
        |*Salve $nameSender\! tudo na paz\?*
        |
        |\/start \- Primeira vez por aqui?
        |\/info \- Precisa de ajuda?
        |\/ouvir \- Ouça: 1 minuto \- Jean Tassy e Marina Sena
    """.trimMargin()

}