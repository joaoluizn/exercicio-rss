package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.IOException
import java.net.URL

class MainActivity : Activity() {

    //ao fazer envio da resolucao, use este link no seu codigo!
//    private val RSS_FEED = "http://leopoldomt.com/if1001/g1brasil.xml"

    //OUTROS LINKS PARA TESTAR...
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        conteudoRSS.layoutManager = LinearLayoutManager(this)

        conteudoRSS.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
//        Tentei fazer o webview, mas n consegui.
    }

    override fun onStart() {
        super.onStart()
        try {
//            Usando ank e doAsync para tratar requisições em outra thread
            doAsync {
                val feedXML = ParserRSS.parse(getRssFeed(getString(R.string.rssfeed)))
                uiThread {
//                  provendo conteudo para a lista através do custom adapter
                    conteudoRSS.adapter = ConteudoRSS(feedXML, it)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

//  Para esse exemplo achei mais elegante apenas usar o padrão de kotlin
//  mas encontrei uma lib chamada khttp que parece ser Bem util e eficiente para projetos maiores.
    @Throws(IOException::class)
    private fun getRssFeed(feed: String): String {
//        Solicitando o conteudo do xml
        return URL(feed).readText()
    }
}
