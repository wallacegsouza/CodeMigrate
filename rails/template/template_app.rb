#require 'fileutils'

#app_name = ask("What's name your application?")

#run "mv #{File.dirname(__FILE__)} #{app_name}"

run "rm public/index"

file ".gitignore", <<-TXT
log/*.log
tmp/**/*
config/database.yml
db/*.sqlite3
.DS_Store
TXT

file "config/locales/pt-BR.yml",
open("http://github.com/svenfuchs/rails-i18n/raw/master/rails/locale/pt-BR.yml").read

git :init
git :add => ".", :commit => %(-m "Initial commit")
