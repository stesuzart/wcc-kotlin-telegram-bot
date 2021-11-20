package bots

import bots.Constant.TOKEN_BOT
import com.vdurmont.emoji.EmojiParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import poi.Sheet.updateSheet
import java.io.File

class WCCBot : TelegramLongPollingBot() {

    private val welcomeMessage = """
       *Olá, tudo bem\?*
       Bem\-vindo ao Future Millionaire
       
       \/start \- começar
       \/create \- criar planilha da riqueza
    """.trimIndent()
    private val incomeMessage = """
        Digite o valor total das suas rendas
        
        Escreva como no exemplo - Substitua a virgula por ponto:
        
        Renda = 2870.0 
    """.trimIndent()
    private val expenditureMessage = """
        Digite o valor total das suas Despesas
        
        Escreva como no exemplo - Substitua a virgula por ponto:
        
        Despesas = 2870.0 
    """.trimIndent()
    private val billsMessage = """
        Digite o valor total das suas dividas vencidas
        
        Escreva como no exemplo - Substitua a virgula por ponto:
        
        Dividas = 2870.0 
    """.trimIndent()

    private val sendDocument = SendDocument()
    private val sendMessage = SendMessage()
    private val dataList = arrayListOf<Double>()

    private val log: Logger = LoggerFactory.getLogger("bot")


    override fun getBotUsername(): String {
        //return bot username
        // If bot username is @HelloKotlinBot, it must return
        return "Future Millionaire Bot"
    }

    override fun getBotToken(): String {
        // Return bot token from BotFather

        return TOKEN_BOT
    }

    override fun onUpdateReceived(update: Update?) {
        // We check if the update has a message and the message has text

        val nameSender = update?.message?.from?.firstName
        val chatId = update?.message?.chatId.toString() // to know for whom send the message
        val messageCommand = update?.message?.text?.lowercase()?.replace(" ", "")

        val message = convertMessage(messageCommand)
        //Pegando a nova planilha que será criada
        val file = File("./planilha_financeira.xlsx")

        log.info(message)

        try {

            when (message) {
                "/start" -> {
                    sendDocument.apply {
                        this.chatId = chatId
                        this.caption = welcomeMessage
                        this.document = InputFile().setMedia(File("src/main/resources/millionaire.gif"))
                        this.parseMode = "MarkdownV2"
                    }
                    execute(sendDocument)
                }
                "/create" -> {
                    sendMessage.apply {
                        this.chatId = chatId
                        this.text = incomeMessage
                    }
                    execute(sendMessage)
                }
                "Income" -> {
                    getDoubleValue(messageCommand)
                    sendMessage.apply {
                        this.chatId = chatId
                        this.text = expenditureMessage
                    }
                    execute(sendMessage)
                }
                "Expenditure" ->{
                    getDoubleValue(messageCommand)
                    sendMessage.apply {
                        this.chatId = chatId
                        this.text = billsMessage
                    }
                    execute(sendMessage)
                }
                "Bills" -> {
                    getDoubleValue(messageCommand)
                    updateSheet(nameSender!!, dataList[0], dataList[1], dataList[2])

                    sendDocument.apply {
                        this.chatId = chatId
                        this.caption = EmojiParser.parseToUnicode("Sua planilha, $nameSender :blush:")
                        this.document = InputFile().setMedia(file)
                        this.parseMode = "MarkdownV2"
                    }
                    execute(sendDocument)
                    //excluindo planilha com os dados do user que a pediu apos envia-la
                    file.delete()
                    log.info("delete temp file")
                }
                else -> {
                    sendDocument.apply {
                        this.chatId = chatId
                        this.caption = EmojiParser.parseToUnicode("Acho que eu não entendi :sad: Vamos recomeçar /start")
                        this.document = InputFile().setMedia(File("src/main/resources/what.gif"))
                    }
                    execute(sendDocument)
                }
            }

        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    private fun convertMessage(message: String?): String{
        return when{
            message!!.contains("/") -> message
            message.contains("=") ->  {
                when {
                    message.contains("renda=") -> "Income"
                    message.contains("despesas=") -> "Expenditure"
                    message.contains("dividas=") -> "Bills"
                    else -> ""
                }
            }else -> ""
        }
    }

    private fun getDoubleValue(text: String?) {
        log.info(text)

        //pega tudo que esta depois do "="
        val number =  text!!.split("=")[1]


        //dataList só pode ter 3 valores (renda, despesas, dividas)
        if(number.isNotEmpty() && dataList.size <= 3){
            log.info(number)
            dataList.add( number.toDouble() )
        } else if(number.isEmpty() && dataList.size <= 3){
            dataList.add(0.0)
        }

    }

}