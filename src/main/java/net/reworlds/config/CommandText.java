package net.reworlds.config;

public class CommandText {
    public static final String helpMessage = """
            📃 <b>RevolutionWorlds | Help</b>
             ├ Список зарегистрированных команд в этом боте:
             ├ /help - Получение всех команд, имеющихся у бота.
             ├ /info - Информация о том, как зайти на сервер.
             ├ /metrics - Информация о сервере (онлайн, тпс)
             ├ /player - Информация об игроке
             ├ /punish - Посмотреть историю банов/мутов игрока (по никнейму или ID)
             ├ /skin - Получение ссылок на скин, плащ и части тела игрока (по никнейму или ID)
             └ /stats - Просмотр статистики игрока (по никнейму или ID)
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
             ├ Информация от $update-time
             ├ Online: $online
             └ TPS: $tps
            """;

    public static final String noPlayerMessage = """
            🔎 <b>RevolutionWorlds | Player</b>
             ├ Вы не указали никнейм игрока.
             └ Пример: <code>/player _Vekster</code>
            """;

    public static final String noIDMessage = """
            🔎 <b>RevolutionWorlds | Player</b>
             ├ Вы не указали id игрока.
             └ Пример: <code>/id 1</code>
            """;

    public static final String unknownPlayerMessage = """
            🔎 <b>RevolutionWorlds | Player</b>
             └ Игрока с никнеймом <code>$player_name</code> не существует.
            """;

    public static final String unknownIDMessage = """
            🔎 <b>RevolutionWorlds | Player</b>
             └ Игрока с ID <code>$id</code> не существует.
            """;

    public static final String playerMessage = """
            🔎 <b>RevolutionWorlds | Player</b>
             ├ [$rank] $player_name
             ├ ID: $id
             ├ DiscordID: $discord_id
             └ UUID: $uuid
                        
            📃 <b>Основная информация</b>
             ├ Первый вход: $first_seen
             ├ Последний вход: $last_seen
             ├ В игре: $playtime
             └ Статус: Оффлайн
                        
            👤 <b>Скин игрока</b>
             └ <a href="https://skin.reworlds.net/raw/skin/$player_name.png">Скачать</a>
            """;

    public static final String noPunishMessage = """
            🔎 <b>RevolutionWorlds | Punish</b>
             ├ Вы не указали никнейм или ID игрока.
             ├ Пример: <code>/player _Vekster</code>
             └ Пример: <code>/id 237</code>
            """;

    public static final String noSkinMessage = """
            🔎 <b>RevolutionWorlds | Skin</b>
             ├ Вы не указали никнейм или ID игрока.
             ├ Пример: <code>/player _Vekster</code>
             └ Пример: <code>/id 237</code>
            """;

    public static final String noStatsMessage = """
            🔎 <b>RevolutionWorlds | Stats</b>
             ├ Вы не указали никнейм или ID игрока.
             ├ Пример: <code>/player _Vekster</code>
             └ Пример: <code>/id 237</code>
            """;

    public static final String unknownPunishMessage = """
            🔎 <b>RevolutionWorlds | Punish</b>
             └ Игрока с никнеймом или id <code>$player</code> не существует.
            """;

    public static final String unknownSkinMessage = """
            🔎 <b>RevolutionWorlds | Skin</b>
             └ Игрока с никнеймом или id <code>$player</code> не существует.
            """;

    public static final String unknownStatsMessage = """
            🔎 <b>RevolutionWorlds | Stats</b>
             └ Игрока с никнеймом или id <code>$player</code> не существует.
            """;

    public static final String punishMessage = """
            🔎 <b>RevolutionWorlds | Player</b>
             ├ [$rank] $player_name
             ├ ID: $id
             ├ DiscordID: $discord_id
             └ UUID: $uuid
             
            🚫 <b>Баны</b>
             └ Этот игрок может играть на сервере.
             
            🔇 <b>Муты</b>
             └ Этот игрок может разговаривать в игровом чате.
            """;

    public static final String skinMessage = """
            🔎 <b>RevolutionWorlds | Player</b>
             ├ [$rank] $player_name
             ├ ID: $id
             ├ DiscordID: $discord_id
             └ UUID: $uuid
             
            👤 <b>Голова</b>
             ├ <a href="https://skin.reworlds.net/head/3d/$player_name.png">3D со 2 слоем</a>
             ├ <a href="https://skin.reworlds.net/helm/$player_name.png">2D со 2 слоем</a>
             ├ <a href="https://skin.reworlds.net/head/3d/$player_name.png">3D</a>
             └ <a href="https://skin.reworlds.net/head/$player_name.png">2D</a>
             
            👤 <b>Тело</b>
             ├ <a href="https://skin.reworlds.net/body/$player_name.png">Спереди</a>
             └ <a href="https://skin.reworlds.net/back/$player_name.png">Сзади</a>
             
            👤 <b>Скин</b>
             ├ <a href="https://skin.reworlds.net/raw/skin/$player_name.png">Скин</a>
             └ <a href="https://skin.reworlds.net/raw/cape/$player_name.png">Плащ</a>
            """;

    public static final String statsMessage = """
            🔎 <b>RevolutionWorlds | Player</b>
             ├ [$rank] $player_name
             ├ ID: $id
             ├ DiscordID: $discord_id
             └ UUID: $uuid
             
            📊 <b>Статистика</b>
             ├ Смертей: $deaths
             ├ Убийств: $kills
             ├- Мобов: $mob_kills
             ├ Сломано блоков: $broken_blocks
             ├ Поставлено блоков $placedBlocks
             └ Достижений: $advancements/102
            """;
}
