import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.equationl.common.gameView.GameView
import com.equationl.common.gameView.chessboardSize
import com.equationl.common.viewModel.AiLevel
import com.equationl.common.viewModel.GameAction
import com.equationl.common.viewModel.gamePresenter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow

fun main() = application {
    val windowState = rememberWindowState()
    windowState.size = DpSize(chessboardSize().dp, (chessboardSize() * 2).dp)

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState
    ) {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                val channel = remember { Channel<GameAction>() }
                val flow = remember(channel) { channel.consumeAsFlow() }
                val state = gamePresenter(flow)

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GameView(
                        chessBoard = state.chessBoardState.chessBoardArray,
                        playerChessNum = state.playerChessNum,
                        aiChessNum = state.aiChessNum,
                        gameState = state.gameState,
                        aiLevel = state.aiLevel,
                        whoFirst = state.whoFirst,
                        onClickChess = { row: Int, col: Int ->
                            channel.trySend(GameAction.ClickChess(row, col))
                        },
                        onRequestNewGame = {
                            channel.trySend(GameAction.ClickRequestNewGame)
                        },
                        onNewGame = { whoFirst: Int, aiLevel: AiLevel ->
                            channel.trySend(GameAction.CLickNewGame(whoFirst, aiLevel))
                        },
                        onTip = {
                            channel.trySend(GameAction.ClickTip)
                        }
                    )
                }
            }
        }
    }
}