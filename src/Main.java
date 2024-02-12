import java.io.*;
import java.nio.file.FileSystemException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws Exception {
        /**
         Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке, разделенные пробелом:
         Фамилия Имя Отчество датарождения номертелефона пол
         Форматы данных:
         фамилия, имя, отчество - строки
         дата_рождения - строка формата dd.mm.yyyy
         номер_телефона - целое беззнаковое число без форматирования
         пол - символ латиницей f или m.
         Приложение должно проверить введенные данные по количеству. Если количество не совпадает с требуемым, вернуть код ошибки, обработать его и показать пользователю сообщение, что он ввел меньше и больше данных, чем требуется.
         Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры. Если форматы данных не совпадают, нужно бросить исключение, соответствующее типу проблемы. Можно использовать встроенные типы java и создать свои. Исключение должно быть корректно обработано, пользователю выведено сообщение с информацией, что именно неверно.
         Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, в него в одну строку должны записаться полученные данные, вида
         <Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
         Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
         Не забудьте закрыть соединение с файлом.
         При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, пользователь должен увидеть стектрейс ошибки.
         */

        try {
            makeRecord();
            System.out.println("Запись произведена");
        } catch (FileSystemException e){
            System.out.println(e.getMessage());
        } catch (IOException e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public static void makeRecord() throws Exception{
        System.out.println("Введите фамилию, имя, отчество, дату рождения (в формате dd.mm.yyyy), номер телефона (число без разделителей) и пол(символ латиницей f или m), разделенные пробелом");

        String text;
        try(BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))) {
            text = bf.readLine();
        } catch (IOException e){
            throw new Exception("Произошла ошибка при работе с консолью");
        }

        String[] array = text.split(" ");
        if (array.length != 6) throw new Exception("Введено неверное количество параметров");

        String lastName = array[0];
        if (!lastName.matches("[\\p{L}| ]+")) throw new Exception("Фамилия должна содержать только буквы");
        String firstName = array[1];
        if (!firstName.matches("[\\p{L}| ]+")) throw new Exception("Имя должно содержать только буквы");
        String middleName = array[2];
        if (!middleName.matches("[\\p{L}| ]+")) throw new Exception("Отчество должно содержать только буквы");

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date birthdate;
        try {
            birthdate = format.parse(array[3]);
            if(birthdate.before(new SimpleDateFormat("dd.MM.yyyy").parse("01.01.1900"))) throw new Exception("Указана неактуальная дата рождения");
        } catch (ParseException e){
            throw new ParseException("Неверный формат даты рождения", e.getErrorOffset());
        }

        long phone;
        try {
            phone = Long.parseLong(array[4]);
        } catch (NumberFormatException e){
            throw new NumberFormatException("Неверный формат телефона");
        }

        String sex = array[5];
        if (!sex.equalsIgnoreCase("m") && !sex.equalsIgnoreCase("f")){
            throw new RuntimeException("Неверно введен пол, должно быть указано значение f или m");
        }

        String fileName = "files\\" + lastName.toLowerCase() + ".txt";
        File file = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file, true)){
            if (file.length() > 0){
                fileWriter.write('\n');
            }
            fileWriter.write(String.format("%s %s %s %s %s %s", lastName, firstName, middleName, format.format(birthdate), phone, sex));
        } catch (IOException e){
            throw new FileSystemException("Возникла ошибка при работе с файлом");
        }
    }
}