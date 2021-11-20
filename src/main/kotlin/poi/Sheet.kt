package poi

import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.io.FileOutputStream

object Sheet {

    //Pegando planilha dentro do projeto (lendo a planilhg)
    private val input = FileInputStream("C:\\Users\\Kacia\\Documents\\wcc-kotlin-telegram-bot\\src\\main\\resources\\planilha_financeira.xlsx")
    private val my_xlsx_workbook = XSSFWorkbook(input)
    private val xlws = my_xlsx_workbook.getSheetAt(0) //Pegar a primeira planilha (Financeiro)

    //Pegar as celulas que quer mudar o valor
    private val cellUserName = xlws.getRow(0).getCell(0)
    private val cellMoneyToSpend = xlws.getRow(4).getCell(3)
    private val cellMoneyToEntertainment = xlws.getRow(5).getCell(3)
    private val cellMoneyToTravel = xlws.getRow(6).getCell(3)
    private val cellIncomeTotal = xlws.getRow(3).getCell(1)
    private val cellExpenditureTotal = xlws.getRow(3).getCell(4)
    private val cellBillsTotal = xlws.getRow(4).getCell(13)

    private val log: Logger = LoggerFactory.getLogger("poi")


    fun updateSheet(userName: String, income: Double, expenditure: Double, bill: Double) {

        log.info("Start changes")

        //mandar os novos valores nas celulas
        cellUserName.setCellValue("Planilha para Cassiana ficar Milionario(a)!!")
        cellMoneyToSpend.setCellValue("$userName (mesada)")
        cellMoneyToEntertainment.setCellValue("$userName Lazer")
        cellMoneyToTravel.setCellValue("$userName Viagem")
        cellIncomeTotal.setCellValue(income)
        cellExpenditureTotal.setCellValue(expenditure)
        cellBillsTotal.setCellValue(bill)

        //variavel para atualizar os valores dentro da planilha (se não só atualiza se voce clicar na celula
        val evaluator: FormulaEvaluator = my_xlsx_workbook.creationHelper.createFormulaEvaluator()
        evaluator.evaluateAll()

        //fechar o arquivo com as mudanças
        input.close()

        //criando uma nova planilha com as mudanças (escrevendo a planilha)
        val outPut = FileOutputStream("./planilha_financeira.xlsx")
        my_xlsx_workbook.write(outPut)
        //fechando a exportação
        outPut.close()

        log.info("Finish changes and update. Create a sheet copy.")

    }

}