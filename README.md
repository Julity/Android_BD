
### AndroidStudio ###
# Охватывает файлы, которые следует игнорировать при разработке Android с использованием Android Studio.

# Встроенные файлы приложений
* .apk 
* .ap_
* .aab

# Файлы для виртуальной машины ART / Dalvik 
*.dex

# Файлы классов Java 
*.class

# Сгенерированные файлы 
bin/
gen/
out/

 файлы # Gradle 
.gradle 
.gradle/
сборка/

# Файлы подписи 
.подписание/

# Локальный файл конфигурации (путь к sdk и т.д.)
local.properties

# Папка Proguard, созданная Eclipse 
proguard/

# Файлы журналов 
* .log

# Android Studio
/*/сборка/
/*/local.properties
/*/out 
/*/*/ сборка
/*/*/ производство 
захваты/
.навигация/
*.ipr 
* ~ 
*.swp

# Файлы хранилища ключей
*.jks 
*.хранилище ключей

# Сервисы Google (например, API или Firebase)
# google-services.json

# Исправление для Android 
gen-external-apklibs

# Внешняя собственная папка сборки, созданная в Android Studio 2.2 и более поздних версиях 
.externalNativeBuild

# NDK
obj/

# IntelliJ IDEA
* .iml 
* .iws 
/out/

# Пользовательские конфигурации 
.идея/кэши/
.идея/ библиотеки/
.идея / полка/
.idea/workspace.xml 
.idea/tasks.xml 
.идея/название 
.idea/compiler.xml 
.idea/copyright/profiles_settings.xml 
.idea/encodings.xml
.idea/misc.xml 
.idea/modules.xml 
.idea/scopes/scope_settings.xml 
.idea/словари 
.idea/vcs.xml 
.idea/jsLibraryMappings.xml 
.idea/datasources.xml 
.idea/Источники данных.идентификаторы 
.idea/sqlDataSources.xml 
.idea/dynamic.xml 
.idea/uiDesigner.xml 
.idea/assetWizardSettings.xml 
.idea/gradle.xml 
.idea/jarRepositories.xml 
.idea/navEditor.xml

# Файлы устаревшего проекта Eclipse 
.путь к классу 
.проект 
.cproject 
.настройки/

# Мобильные инструменты для Java (J2ME) 
.mtj.tmp/

# Файлы пакетов #
*.war 
*.ear

# журналы сбоев виртуальной машины (ссылка: http://www.java.com/en/download/help/error_hotspot.xml)
hs_err_pid*

## Файлы, относящиеся к плагину:

# mpeltonen / sbt-плагин idea 
.idea_modules/

# Плагин JIRA 
atlassian-ide-plugin.xml

# Плагин Mongo Explorer 
.idea/mongoSettings.xml

# Плагин Crashlytics (для Android Studio и IntelliJ)
com_crashlytics_export_strings.xml 
crashlytics.свойства 
crashlytics-сборка.свойства 
fabric.свойства

### Исправление для AndroidStudio ###

! /gradle/wrapper/gradle-wrapper.jar

# Конец https://www.toptal.com/developers/gitignore/api/androidstudio
