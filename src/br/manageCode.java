package br;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class manageCode {

<<<<<<< HEAD
=======

>>>>>>> master
    HashMap<String,Entidade> mapEntidade = new HashMap<String, Entidade>();
    List<String> tipos = new ArrayList<String>();

    manageCode(){
        addTipos(
            "String",
            "Integer",
            "boolean",
            "Long",
            "byte",
            "Double",
            "Float",
            "Set;",
            "Date"
        );
    }

    void addTipos(String... tipos){
        for (String t : tipos) this.tipos.add(t);
    }

    /*
        Classes Internas
    */

    abstract class DomainObj{
        public int hashCode() {
            return toString().hashCode();
        }
        public boolean equals(Object obj) {
            return toString().equals(obj.toString());
        }
        public String toString() {
            ArrayList<String> list = new ArrayList<String>();
            Field f[] = this.getClass().getDeclaredFields();
            AccessibleObject.setAccessible(f, true);

            try {
                for (int i = 0; i < f.length-1; i++) 
                    list.add(f[i].getName() + "=" + f[i].get(this));

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return this.getClass().getName().concat(list.toString());
        }
    }

    class  ProjectConfig extends DomainObj {
        String name, basePath, database, dbUser, dbPassword;
    }

    /**
     * @author wallaceant
     * @version 1.0.0
     * <br>
     * Classe que representa o meta dado de uma classe.
     * 
     * <pre>
     * Uma entidade, ou objeto, possui atributos e relacionameto
     *  - atr
     * </pre>
     * 
     */
    class Entidade extends DomainObj{
        String nome, superClass, pack_age;
        List<Relacionamento> relacionamentos;
        List<Atributo> atributos;

        Entidade(String nome, String pack_age){
            this.nome = nome;
            relacionamentos = new ArrayList<Relacionamento>();
            atributos = new ArrayList<Atributo>();
            this.pack_age = pack_age;
        }
    }

    class Relacionamento extends DomainObj{
        String entity1, entity2, cardinalidade;

        Relacionamento(String... args){
            entity1 = args[0];
            entity2 = args[1];
            cardinalidade = args[2];
        }
    }

    class Atributo extends DomainObj{
        String nome, tipo, anotacao;
        //List<String> anotacoes;

        Atributo(String... args){
            nome = args[0];
            tipo = args[1];
            //this.anotacoes = anotacoes;
            anotacao = args[2];
        }

        Atributo (){}
    }

    /*
        Manipuladores e metodos acessores
    */

    Entidade newEntidade(String nome, String pack_age){
        return new Entidade(nome, pack_age);
    }

    /**
     * @param nome
     * @param tipo
     * @param anotacoes - todas as anotacoes deste atributo
     */
    Atributo newAtributo(String nome, String tipo,String anotacao){
        return new Atributo(nome, tipo, anotacao);
    }

    Relacionamento createOneToMany(String entityOne, String entityMany){
        return new Relacionamento(entityOne, entityMany, "@OneToMany");
    }

    Relacionamento createOneToOne(String entity1, String entity2){
        return new Relacionamento(entity1, entity2, "@OneToOne");
    }

    // inicialmente vou gerar o script para o spingRoo
    // com varias configuracoes padrao
    // ainda nao esta sendo feito a verificacao das anotacoes
    void springRooCodeGenerator(Entidade... entitys){
        StringBuilder scriptRoo = new StringBuilder();
        scriptRoo.append("project --topLevelPackage "+ config.basePath);
        if ( (config.database != null) ) {}
        scriptRoo.append(
        "persistence setup --provider HIBERNATE " +
        " --database HYPERSONIC_PERSISTENT");

        // criando as entidades
        for(Entidade e : entitys){
             scriptRoo.append("entity --name ~."+e.pack_age+"."+e.nome +
             " --testAutomatically");
        }

        // adicionando os atributos
        // 100 as anotacoes
        for(Entidade e : entitys){
            for(Atributo a : e.atributos){
                scriptRoo.append("field "+a.tipo.toLowerCase()+
                "--class ~."+ e.pack_age +"."+ e.nome +" --fieldName "+ a.nome);
            }
            scriptRoo.append("\n");
        }

        // criar relacionamentos
        // verificar o erro
        for(Entidade e : entitys){
            String eName = "~."+ e.pack_age +"."+ e.nome;
            for(Relacionamento r : e.relacionamentos){
                scriptRoo.append("field reference --class "+ eName
                "--fieldName "+ r.entity2 +"."+ e.nome +" --fieldName "+ a.nome);
            }
            scriptRoo.append("\n");
        }

    }

    void rubyOnRailsCodeGenerator(Entidade... entitys){
        System.exec("ruby ../../../rails/template/template_app.rb");
    }

    void djangoCodeGenerator(ProjectConfig config, Entidade... entitys){
    }

    public String removeSpaceTabEndLine(String line){
        int end = line.length();
        for(int i = line.length() - 1; i >=0 ; i--){
             if ( (line.charAt(i) != " " ) && (line.charAt(i) != "	" ) ){
                end = i;
                break;
             }
        }
        return line.substring(0, end);
    }

    void builderModel(String url) throws MalformedURLException, IOException{

        File file = new File(url);
        InputStreamReader inReader;
        inReader = new InputStreamReader(file.toURI().toURL().openStream());
        BufferedReader read = new BufferedReader(inReader);
        String
            line = "",
            objTurn = "",
            pack_age = "";

        String[] lineClass;

        /*
            Valor do codigo ascii dos carracteres utilizados para
            a identação do codigo
            tab = 9
            space = 32
            > = 62
            < = 60
            @ = 64
        */
        while ((line = read.readLine()) != null){

            line = removeSpaceTabEndLine(line);

            //se a linha não estiver vazia
            if (line.length() > 0){

                //verifica se a linha é um comentario
                //da forma q esta só aceita linhas puras 100 comentarios no final
                if (line.contains("//")) continue;

                //validação de erro deve ser feita
                //exp: trocar 4space por tab; remover ';'
                int characterOne =line.codePointAt(0);

                //pega o nome das classes
                if (( characterOne != 9) && (characterOne != 32)){
                    lineClass = line.split(" ");

                    if(!lineClass[0].equals("package")){
                        objTurn = lineClass[0];
                        System.out.println(objTurn);

                        //talvez esse if não tenha nescessidade
                        if (!mapEntidade.containsKey(objTurn)){
                            mapEntidade.put(objTurn, newEntidade(objTurn, pack_age));
                        }

                    }else {
                        pack_age = lineClass[1];
                        System.out.println(pack_age);
                    }

                } else {

                    String[] comandLine = line.replaceAll("\\s+", " ").split(" ");
                    // se o primeiro caracter da linha for tab
                    // logo sera 1 atributo ou uma anotacao
                    // ainda não foi implementado a lista de anatocao para um atributo
                    if (line.codePointAt(0) == 9){
                        //String[] comandLine = line.replaceAll("\\s+", " ").split(" ");
                        Atributo attribute = null;
                        Entidade entity = mapEntidade.get(objTurn);

                        // com anotacao
                        if (line.contains("@")){

                            // se o atributo não contem na lista de tipos
                            if (!tipos.contains(comandLine[3])){
                                System.out.println(" Atributo com antocao "+ comandLine[3]+" "+ comandLine[2]+" " +comandLine[1]);
                                attribute = newAtributo(comandLine[3], comandLine[2], comandLine[1]);
                                entity.atributos.add(attribute);
                                Relacionamento relationship = null;

                                //criar relacionamento *-1
                                if (attribute.anotacao.contains("@M")){
                                    //String[] aux = a.tipo.replace("<", ",").replace(">", ",").split(",");
                                    relationship = createOneToMany(objTurn, attribute.nome);

                                } else {
                                    //criar relacionamento 1-1
                                    if (attribute.anotacao.contains("@O"))
                                        relationship = createOneToOne(objTurn, attribute.nome);
                                }

                                entity.relacionamentos.add(relationship);
                            }

                        } else {
                            //100 anotacao
                            if (!tipos.contains(comandLine[0])){
                                System.out.println("//100 anotacao "+ comandLine[2] + comandLine[1]);
                                attribute = newAtributo(comandLine[2], comandLine[1], "");
                                entity.atributos.add(attribute);
                            }
                        }
                    }// end if tab

                }
            }

        }

    }

    int entityNumber(){
        return mapEntidade.values().size();
    }

    /*
        Main
    */
    public static void main(String[] args) throws MalformedURLException, IOException {

        manageCode mc = new manageCode();
        System.out.println("args.length = "+ args.length);

        if(args.length > 0){
            for (int i = 0; i < args.length; i++) mc.builderModel(args[i]);
            System.out.println(mc.entityNumber());
        }
    }
}
