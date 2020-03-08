package HTAlgoInsertionSort

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import freemarker.cache.*
import io.ktor.freemarker.*
import io.ktor.http.content.*
import io.ktor.sessions.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    routing {
        get("/") {
            call.respondText("New ALGORITHM SELECTION SORT, entrez 'html-dsl' dans l'URL", contentType = ContentType.Text.Plain)
        }       // chemin pour afficher NEW ALGO ...

        get("/html-dsl") {
            call.respondHtml {
                body {
                    h1 { +"INSERTION SORT ALGORITHM"}
                    /*ul {
                        for (n in 1..10) {
                            li { +"$n" }
                        }
                    }*/
                    div {
                        p { +"Entrer une liste de 5 nombres :" }
                        form("/response", method = FormMethod.post) {
                        /*
                        Boucle pour choisir le nombre d'éléments dans la liste(pas réalisée)
                        for (n in 1..5) {
                            input { +"$n"
                                type = InputType.number
                                id ="tab1"
                                name = "tab1"
                                min = "0"
                                max ="100"}
                        }
                        */

                            input {
                                type = InputType.number
                                id ="tab1"
                                name = "tab1"
                                min = "0"
                                max ="100"
                            }

                            input {
                                type = InputType.number
                                id ="tab2"
                                name = "tab2"
                                min = "0"
                                max ="100"
                            }

                            input {
                                type = InputType.number
                                id ="tab3"
                                name = "tab3"
                                min = "0"
                                max ="100"
                            }

                            input {
                                type = InputType.number
                                id ="tab4"
                                name = "tab4"
                                min = "0"
                                max ="100"
                            }

                            input {
                                type = InputType.number
                                id ="tab5"
                                name = "tab5"
                                min = "0"
                                max ="100"
                            }

                        button {
                            type = ButtonType.submit
                            }

                        }
                    }
                }
            }       // var mylist = call.receiveParameters()["nombre"]
                    // ici le chemin est /html-dsl
        }

        post("/response") {

            call.respondHtml {

                body {
                    h1 {
                        +"Résultat de votre liste triée :$result"
                    }
                }

                // la page du chemin /response ne charge pas si le call.respondHtlm est en bas dans le "post", impossible donc
                // de récupérer le résultat de l'algo "result" qui se situe plus bas
            }

            // essaie de récupérer les valeurs entrées dans la premiere page pour créer un array de STRING qui serait
            // transformé en array de INT

            var list1 = call.receiveParameters()["tab1"]
            var list2 = call.receiveParameters()["tab2"]
            var list3 = call.receiveParameters()["tab3"]
            var list4 = call.receiveParameters()["tab4"]
            var list5 = call.receiveParameters()["tab5"]

            list1!!.toInt()
            list2!!.toInt()
            list3!!.toInt()
            list4!!.toInt()
            list5!!.toInt()

            val lister: Array<String> = arrayOf(list1, list2, list3, list4, list5)
            val result = insertionSort(lister)
            // je n'arrive pas a créer le tableau en array<Int>


        }

        /*
        post("/response") {
            /*val lister = call.receiveParameters()["tab"]
            insertionSort(lister!!.toIntArray())
            val result = insertionSort(lister!!.toIntArray())*/

            call.respondHtml {


                body {
                    h1 { +"Votre liste triée :" }
                    p { +"Valeur : $result" }

                }

            }
        }// ici le chemin est /response */



        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.red
                }
                p {
                    fontSize = 2.em
                }
                rule("p.myclass") {
                    color = Color.blue
                }
            }
        }

        get("/html-freemarker") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("data" to IndexData(listOf(1, 2, 3))), ""))
        }

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }

        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }
}

// Algorithm Insertion Sort
fun insertionSort(parameterArray: Array<String>) : Array<String> {  // modifié en String pour tenter de faire fonctionner l'algo

    var a = parameterArray
    for (x in 1 until a.size)	{
        var y = x
        var z = y-1
        while (y > 0 && a[y] < a[y-1]) {
            run {val temp = z; z = y; y = temp}
            y=-1
        }
    }

    return a
}

data class IndexData(val items: List<Int>)

data class MySession(val count: Int = 0)

fun FlowOrMetaDataContent.styleCss(builder: CSSBuilder.() -> Unit) {
    style(type = ContentType.Text.CSS.toString()) {
        +CSSBuilder().apply(builder).toString()
    }
}

fun CommonAttributeGroupFacade.style(builder: CSSBuilder.() -> Unit) {
    this.style = CSSBuilder().apply(builder).toString().trim()
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

