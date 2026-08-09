# Trading Post + Easy Villagers Compatibility

Target versions:

- Minecraft 1.21.1
- NeoForge 21.1.x
- Trading Post 21.1.1
- Easy Villagers 1.1.42
- Infinite Trading 5.0 (optional)

The mixin extends Trading Post's normal entity lookup with villagers contained in
Easy Villagers `TraderTileentityBase` block entities. It passes the original
`EasyVillagerEntity` to Trading Post, so trades and Infinite Trading mixins operate
on the real stored villager and its real offers.

## Building

Create a `libs` directory and copy these exact dependency jars into it:

- `TradingPost-v21.1.1-1.21.1-NeoForge.jar`
- `easy-villagers-neoforge-1.21.1-1.1.42.jar`

Then run `gradle build` with Java 21. The ready-to-install jar delivered alongside
this source archive was compiled directly against the exact jars from the ATM10
instance.
