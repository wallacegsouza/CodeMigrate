app_name = ask("What's name your application?")

run "rails #{app_name} -m ~/CodeMigrate/rails/template/template_app.rb"

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
