# tLib
tLib is a library focused on improving the readability, performance, security, and time 
consumed on the basic plugin functions, such as **Commands** or **Menus**.
<br/>
Currently, the library has been tested on **Spigot 1.8**, but it should work for every version.

# Usage
In order to use the project, you must add it as a maven compile dependency:

```xml
<!--Add the tLib repository-->
<repositories>
  <repository>
    <id>repsy</id>
    <url>https://repo.repsy.io/mvn/tehin/tlib</url>
  </repository>
</repositories>

<!--Add dependency of the latest Spigot library version-->
<dependencies>
  <dependency>
    <groupId>dev.tehin</groupId>
    <artifactId>tlib-spigot</artifactId>
    <version>1.3.1</version>
  </dependency>
</dependencies>
```
Or, if you prefer to use our library in BungeeCord:
```xml
<!--Add the tLib repository-->
<repositories>
  <repository>
    <id>repsy</id>
    <url>https://repo.repsy.io/mvn/tehin/tlib</url>
  </repository>
</repositories>

<!--Add dependency of the latest BungeeCord library version-->
<dependencies>
  <dependency>
    <groupId>dev.tehin</groupId>
    <artifactId>tlib-bungee</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

# Getting Started
Since the library handles everything in the background, implementations are really straightforward. 
Nearly every option is configurable inside the API itself.
<br/>

Every class and function is documented inside the jar, for any extra information visit the docs, 
the following explanation will be very brief and basic.
<br/>

For using the library, you must build it and register your classes, as follows:

```java
package dev.tehin.example;

public class MainPlugin extends JavaPlugin {
    
    private static @Getter tLib lib;

    @Override
    public void onEnable() {
        /* 
         * Build and set the library as a static method for later usage
         * (It is not required to be static)
         * 
         * In this example, later usage would look like:
         * 
         * MainPlugin.getLib().getMenu().open(Player, Menu)
         */
        lib = loadLib();

        // After building the library, you must register your commands and menus.
        registerCommands();
        registerMenus();
    }
    
    private tLib loadLib() {
        /*
         * If you don't need to use any specific configuration
         * you can use the default ones (do not specify the second argument)
         * 
         * Example of an instnace without any configuration:
         * 
         * return tLib.build(this);
         */
        
        // Create and modify the instance of task configurations (all values are optional)
        TasksConfig tasks = new TasksConfig()
                .corePoolSize(2)
                .maximumPoolSize(4);

        // Create the desired configuration (optional)
        LibConfiguration config = new LibConfiguration()
                .tasks(tasks);
        
        // Builds the library for usage of this plugin
        return tLib.build(this, config);
    }

    private void registerCommands() {
        CommandBase[] commands = {
            new HostCommand(),
            new GlobalCommand(),
            // [All Your Commands ...]
        };

        lib.getCommand().register(commands);
    }

    private void registerMenus() {
        Menu[] menus = {
          new HostMenu(),
          new ParticlesMenu(),
          // [All Your Menus ...]      
        };
        
        lib.getMenu().register(menus);
    }
}
```

# Examples
Here we will display all the examples possible, using most of the options available for each case
<br/>

## Commands
Creating a command is as easy as implementing one method, and using one required annotation argument:
- ```@CommandProperties``` **(Only 'path' is required)**:
  - *path* - **String**: The path to be used, "." will separate sub-commands (example: "host.show", will be: "/host show")
  - *executors* - **Class<? extends CommandSender>**: What commands sender can execute the command
  - *permission* - **String**: What permission should the player have to execute this command
- ```@CommandArgsStructure```: (Optional):
  - *usage* - **String**: This will be the usage message when the command was not correctly executed
  - *fixedLength* - **Integer**: When you don't have a variable length, this will define the bounds of the command
  - *structure* - **Class<?>[]**: The arguments must be assignable from the represented class (in order)
- ```@CommandDescription```: The command description, shown in /help or the sub-command map
- ```@CommandAliases```: Array of strings which will represent the aliases, this cannot be used in sub-commands

```java
package dev.tehin.example;

@CommandProperties(
        path = "host",
        executors = {Player.class},
        permission = "events.host"
)
@CommandArgsStructure(
        usage = "host <target>",
        fixedLength = 1,
        structure = {String.class}
)
@CommandDescription("Open the host menu for hosting an event")
@CommandAliases({"share", "play", "game"})
public class HostCommand implements CommandBase {

  /**
   * This will open the host menu to the target.
   *
   * Since fixed length is 1, any other parameter will send the defined usage.
   *
   * This command can only be executed by the Player class, any console senders will
   * get a "not permitted" message.
   *
   * The command description can be configured via the API, if it should show only for
   * sub-commands, or also in a general /help command.
   *
   * @param args The command args, including the sender, length, and string arguments
   */
    public void execute(CommandArgs args) {
        Player sender = (Player) args.getSender();
        Player target = Bukkit.getPlayer(args.getArg(0));
    
        MainPlugin.getLib().getMenu().open(target, HostMenu.class);
    }
}
```

## Menus
For now, the inventory is created when the player opens it, this in the future will be
optional, giving support to static inventories.
<br/>

For creating a menu, you only need one annotation, then, we provide to you
our easy-to-use item builder, utilities, and menu actions.
- ```@CommandProperties```:
  - *display* - **String**: What should the menu display be (accepts chat colors)
  - *permission* - **String**: Do not assign this parameter if no permission is required, 
  otherwise, the player must have the permission in order to open the menu

```java
package dev.tehin.example;

@MenuProperties(display = "&9&lHost Event")
public class HostMenu extends Menu {

  /**
   * This will contain the inventory contents.
   * Inventory size will be calculated based on the size returned.
   *
   * Below we will try to use as many utilities as possible,
   * to showcase how you should create an inventory with our library.
   *
   * @param player The player opening the inventory
   * @return Array containing the inventory items in order 
   */
  @Override
  protected ItemStack[] create(Player player) {
    List<ItemStack> items = new ArrayList<>();

    // Adds a 9 item glass row
    ItemUtils.addGlassRow(items);

    /*
     * Adds the item with their respective spacing.
     *
     * The first argument is the list that will be modified.
     * The second argument are the items to be distributed.
     *
     * For example, with 3 items:
     * air, air, air, item, air, item, air, air, air
     */
    ItemUtils.addWithSpaces(items, getEvents(player));
    ItemUtils.addWithSpaces(items, getOptions(player));

    // Glass row
    ItemUtils.addGlassRow(items);

    return items.toArray(new ItemStack[0]);
  }

  /**
   * Example of command based items
   */
  private List<ItemStack> getEvents(Player player) {
    List<ItemStack> result = new ArrayList<>();

    // Example enum that contains a list of events
    for (EventType type : EventType.values()) {
      ItemBuilder builder = new ItemBuilder(Material.ARROW)
              .name("&a&l" + type.name()) // The item display
              .glow(true) // Adds "enchanted" effect
              .lore("", type.getDesc(), "", "&aClick to start"); // The lore to have

      String command = type.name() + " start"; // Command without "/"

      /*
       * The item will be created from the builder given.
       * The player will execute the command on click.
       *
       * Returns the built item to be added to the inventory.
       */
      ItemStack item = craft.asCommand(builder, command);

      result.add(item);
    }
    return result;
  }

  private List<ItemStack> getOptions(Player player) {
      List<ItemStack> result = new ArrayList<>();
  
      ItemBuilder navigate = new ItemBuilder(Material.REDSTONE)
              .name("&a&lNavigate") // Display
              .amount(16) // Item amount
              .lore("", "&7Navigate to other inventory", ""); // Lore
  
      /*
       * Navigable works the same as the command.
       * It returns the built item stack.
       * It will navigate to the given menu on click.
       */
      result.add(craft.asNavigable(nagivate, OtherMenu.class));
  
      ItemBuilder clickable = new ItemBuilder(Material.LEATHER_CHESTPLATE)
              .name("&a&lClick") // The display
              .color(DyeColor.RED); // Color of the item (works with leather)
  
      // The consumer to be executed on click
      Consumer<Player> action = (clicker) -> {
          clicker.sendMessage("You have clicked the inventory!");
      };
  
      // The same as navigable or clickable, action will be executed on click
      results.add(craft.asClickable(clickable, action));
    }
}
```

## Utilities
The library has many extra utilities that solve many of the problems a developer 
may encounter, they should some hours of unnecessary code.

### ItemBuilder
This builder is given to the ItemProvider in order to get actions inside our
menus.
<br/>

The builder creates an ItemStack based on various optional options, such as:
- Color
- Display Name
- Enchanted Effects
- Lore
- And many more...

```java
public void example() {
    // TODO
}
```

### TaskUtil
This util provides everything related to Bukkit tasks, such as:
- Running a **sync** task in the Bukkit thread.
- Running a **sync** task on the next tick in the Bukkit thread.
- Running a **delayed sync** task in the Bukkit thread.
- Running an **async** task in the ForkJoinPool.
- Running a **delayed async** task in the ForkJoinPool.

```java
public void example() {
    // TODO
}
```

### TaskTimer
This is a class that provides functionality to repeating tasks, it has many options,
everything is documented, so you will have no problem in understanding each parameter.
<br/>

The timer executes a task (async or sync), each tick (which duration is specified
on creation), for a defined period of time.
<br/>

This comes in handy when creating particle effects, victory effects, or a timer
before sending a player to the spawn, it saves a lot of time in various
tasks.

```java
public void example() {
    // TODO
}
```

### ItemUtil
This util is specially used when creating menus with our library,
examples were show above.
<br/>

Provides many utils related to ItemStacks and menus, such as:
- Get and add NBT Tags to an item
- Add glow to an item
- Given a List of items, adds spacing, empty or glass rows.
- Add color or enchantments to an item

```java
public void example() {
    // TODO
}
```



