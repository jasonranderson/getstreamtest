package ai.ander.quantum5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.enqueue
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.ui.channel.ChannelListFragment
import io.getstream.chat.android.ui.message.MessageListActivity
import io.getstream.chat.android.ui.message.MessageListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiKey = "<API-KEY>"
        val token = "<TOKEN>"

        val offlinePluginFactory = StreamOfflinePluginFactory(
            config = Config(
                backgroundSyncEnabled = true,
                userPresence = true,
                persistenceEnabled = true,
                uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING
            ),
            appContext = applicationContext
        )
        val client = ChatClient.Builder(apiKey, applicationContext)
            .logLevel(ChatLogLevel.ALL)
            .withPlugin(offlinePluginFactory)
            .build()
        val user = User(
            id = "<USER-ID>"
        )

        client.connectUser(user, token).enqueue { result ->
            if (result.isSuccess) {
                if (savedInstanceState == null) {

//                    this.startActivity(MessageListActivity.createIntent(this, "messaging:channelId"))
                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.container, MessageListFragment.newInstance("messaging:channelId")) // instantiating this fragment won't update when the app comes back into foreground
                        .replace(R.id.container, ChannelListFragment.newInstance()) // instantiating this fragment seems to get all updates from the messages sent while in background, even if backgounding/foregrounding when the message list still in view
                        .commit()
                }
            }
        }
    }
}