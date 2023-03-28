package net.reworlds.config;

public class CommandText {
    public static final String helpMessage = """
            📃 <b>RevolutionWorlds | Help</b>
             ├ Список зарегистрированных команд в этом боте:
             ├ /help - Получение всех команд, имеющихся у бота
             ├ /info - Информация о том, как зайти на сервер
             ├ /metrics - Информация о сервере (онлайн, тпс)
             ├ /user -Информация об игроке (по никнейму или ID)
             ├ /skin - Получение ссылок на скин, плащ и части тела игрока (по никнейму или ID)
             └ /coin - Команда для развлечения
            """;

    public static final String infoMessage = """
            🔐 <b>RevolutionWorlds | Help</b>
             ├ Для начала вам нужно зарегистрировать ваш аккаунт через бота на нашем <a href="https://discord.gg/vuFwdJ8">Discord сервере</a>.          
             ├ ❗️ <b>Bedrock</b>: После регистрации необходимо обратиться к администратору Kynth#6029 в личном сообщении в Discord.         
             ├ ❗️ <b>Java</b>: После регистрации необходимо установить мод ReProtect на одну из модификаций Minecraft'а в <a href="https://discord.com/channels/682240333366558732/1083755321576665189">Discord канале #ReProtect</a>         
             ├ После установки модификации или после ответа от администратора вы можете играть, присоединившись к одному из IP ниже:
             ├ play.reworlds.net
             ├ eu.reworlds.net
             ├ play.reworlds.net:19132 (Bedrock 1.19.62)
             └ Приятной игры на нашем проекте!
            """;

    public static final String metricsMessage = """
            🌍 <b>RevolutionWorlds | Metrics</b>
             ├ Информация от %1$s
             ├ Online: %2$d
             └ TPS: %3$s
            """;

    public static final String noUserMessage = """
            🔎 <b>RevolutionWorlds | User</b>
             ├ Вы не указали никнейм или id игрока.
             ├ Пример: <code>/user _Vekster</code>
             └ Пример: <code>/user 1</code>
            """;

    public static final String unknownUserMessage = """
            🔎 <b>RevolutionWorlds | User</b>
             └ Игрока с никнеймом или id <code>%1$s</code> не существует.
            """;

    public static String userMessage = """
            🔎 <b>RevolutionWorlds | User</b>
             ├ [%1$s] %2$s %18$s
             ├ ID: %3$d
             ├ DiscordID: <a href="https://discordapp.com/users/%4$d">%4$d</a>
             └ UUID: %5$s
                        
            📃 <b>Основная информация</b>
             ├ Первый вход: %6$s
             ├ Последний вход: %7$s
             ├ В игре: %8$s
             └ Статус: %9$s
             
            📊 <b>Статистика</b>
             ├ Смертей: %10$d
             ├ Убийств: %11$d
             ├ Мобов: %12$d
             ├ Сломано блоков: %13$d
             ├ Поставлено блоков %14$d
             └ Достижений: %15$d/102
             
            🚫 <b>Баны</b>%16$s
                        
            🔇 <b>Муты</b>%17$s
                        
            👤 <b>Скин игрока</b>
             └ <a href="https://skin.reworlds.net/raw/skin/%2$s.png">Скачать</a>
            """;

    public static final String noSkinMessage = """
            🔎 <b>RevolutionWorlds | Skin</b>
             ├ Вы не указали никнейм или ID игрока.
             ├ Пример: <code>/skin _Vekster</code>
             └ Пример: <code>/skin 1</code>
            """;

    public static final String unknownSkinMessage = """
            🔎 <b>RevolutionWorlds | Skin</b>
             └ Игрока с никнеймом или id <code>%1$s</code> не существует.
            """;

    public static final String skinMessage = """
            🔎 <b>RevolutionWorlds | Skin</b>
             ├ [%1$s] %2$s
             ├ ID: %3$d
             ├ DiscordID: <a href="https://discordapp.com/users/%4$d">%4$d</a>
             └ UUID: %5$s
             
            👤 <b>Голова</b>
             ├ <a href="https://skin.reworlds.net/head/3d/%2$s.png">3D со 2 слоем</a>
             ├ <a href="https://skin.reworlds.net/helm/%2$s.png">2D со 2 слоем</a>
             ├ <a href="https://skin.reworlds.net/head/3d/%2$s.png">3D</a>
             └ <a href="https://skin.reworlds.net/head/%2$s.png">2D</a>
             
            👤 <b>Тело</b>
             ├ <a href="https://skin.reworlds.net/body/%2$s.png">Спереди</a>
             ├ <a href="https://skin.reworlds.net/back/%2$s.png">Сзади</a>
             └ <a href="https://api.simplykel.ru/skin/render?name=%2$s&api=7&sendfile=true">3D Рендер</a>
             
            👤 <b>Скин</b>
             ├ <a href="https://skin.reworlds.net/raw/skin/%2$s.png">Скин</a>
             └ <a href="https://skin.reworlds.net/raw/cape/%2$s.png">Плащ</a>
            """;

    public static String noAccountMessage = """
            👤 <b>RevolutionWorlds | Account</b>
             ├ Вы не указали никнейм или ID игрока.
             ├ Пример: <code>/account _Vekster</code>
             └ Пример: <code>/account 1</code>
            """;

    public static String unknownAccountMessage = """
            👤 <b>RevolutionWorlds | Account</b>
             └ Игрока с никнеймом или id <code>%1$s</code> не существует.
            """;

    public static String accountMessage = """
            👤 <b>RevolutionWorlds | Account</b>
             ├ Успешная синхронизация аккаунта!
             └ Вы можете использовать команды /user и /skin можно использовать без аргументов!
            """;

    // Fun commands

    public static final String noCoinMessage = """
            🪙 <b>Fun | Coin</b>
             ├ Вы не указали ставку. Ставка должна быть целым числом. (от 61 до 31622399)
             └ Пример: <code>/coin 61</code>
            """;

    public static final String unknownCoinMessage = """
            🪙 <b>Fun | Coin</b>
             ├ Некорректная ставка. Ставка должна быть целым числом. (от 61 до 31622399)
             └ Пример: <code>/coin 61</code>
            """;

    public static final String coinMessage = """
            🪙 <b>Fun | Coin</b>
             ├ Выпало: %1$s
             └ %2$s
            """;
}
