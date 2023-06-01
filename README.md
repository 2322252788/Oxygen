# Oxygen-1.8.9
一个使用Mixin注入实现的作弊客户端, 支持MinecraftForge(Version 1.8.9)

# About
该项目起源于2020年，已于2021年结束支持

# 构建方法
1. 使用git fork本项目于你的计算机
2. 打开本项目的文件夹
3. 使用gradle指令构建项目
     - 使用IDEA: `gradlew setupDevWorkspace idea genIntellijRuns build`
     - 使用Eclipse: `gradlew setupDevWorkspace eclipse`
4. 在IDE打开本gradle项目

**若无法运行请添加VM变量**`-Dfml.coreMods.load=cn.rainbow.oxygen.injection.MixinLoader`
