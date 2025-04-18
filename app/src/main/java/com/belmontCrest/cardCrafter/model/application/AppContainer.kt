package com.belmontCrest.cardCrafter.model.application

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.belmontCrest.cardCrafter.localDatabase.database.FlashCardDatabase
import com.belmontCrest.cardCrafter.localDatabase.dbInterface.repositories.CardTypeRepository
import com.belmontCrest.cardCrafter.localDatabase.dbInterface.repositories.FlashCardRepository
import com.belmontCrest.cardCrafter.localDatabase.dbInterface.repositories.OfflineCardTypeRepository
import com.belmontCrest.cardCrafter.localDatabase.dbInterface.repositories.OfflineFlashCardRepository
import com.belmontCrest.cardCrafter.localDatabase.dbInterface.repositories.OfflineScienceRepository
import com.belmontCrest.cardCrafter.localDatabase.dbInterface.repositories.ScienceSpecificRepository
import com.belmontCrest.cardCrafter.supabase.model.daoAndRepository.repositories.AuthRepository
import com.belmontCrest.cardCrafter.supabase.model.daoAndRepository.repositories.AuthRepositoryImpl
import com.belmontCrest.cardCrafter.supabase.model.daoAndRepository.repositories.ImportRepository
import com.belmontCrest.cardCrafter.supabase.model.daoAndRepository.repositories.ImportRepositoryImpl
import com.belmontCrest.cardCrafter.supabase.model.daoAndRepository.repositories.SBTableRepositoryImpl
import com.belmontCrest.cardCrafter.supabase.model.daoAndRepository.repositories.SBTablesRepository
import com.belmontCrest.cardCrafter.supabase.model.daoAndRepository.repositories.OfflineSupabaseToRoomRepository
import com.belmontCrest.cardCrafter.supabase.model.daoAndRepository.repositories.SupabaseToRoomRepository
import com.belmontCrest.cardCrafter.supabase.model.daoAndRepository.repositories.UserExportDecksRepositoryImpl
import com.belmontCrest.cardCrafter.supabase.model.daoAndRepository.repositories.UserExportedDecksRepository
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope

/** Creating our App Container which will get the repositories,
 * who have all our Dao interfaces, which are tied by the database.
 * And the supabase functions to get our online tables
 */
interface AppContainer {
    val flashCardRepository: FlashCardRepository
    val cardTypeRepository: CardTypeRepository
    val scienceSpecificRepository: ScienceSpecificRepository
    val supabaseToRoomRepository: SupabaseToRoomRepository
    val sbTablesRepository: SBTablesRepository
    val importRepository: ImportRepository
    val authRepository: AuthRepository
    val userExportedDecksRepository: UserExportedDecksRepository
    val sharedSupabase: SupabaseClient
    val syncedSupabase: SupabaseClient
}

@RequiresApi(Build.VERSION_CODES.O)
class AppDataContainer(
    private val context: Context,
    scope: CoroutineScope,
    sharedSupabase: SupabaseClient,
    syncedSupabase: SupabaseClient
) : AppContainer {
    override val flashCardRepository: FlashCardRepository by lazy {
        OfflineFlashCardRepository(
            FlashCardDatabase.Companion.getDatabase(context, scope).deckDao(),
            FlashCardDatabase.Companion.getDatabase(context, scope).cardDao(),
            FlashCardDatabase.Companion.getDatabase(context, scope).savedCardDao()
        )
    }
    override val cardTypeRepository: CardTypeRepository by lazy {
        OfflineCardTypeRepository(
            FlashCardDatabase.Companion.getDatabase(context, scope).cardTypes(),
            FlashCardDatabase.Companion.getDatabase(context, scope).basicCardDao(),
            FlashCardDatabase.Companion.getDatabase(context, scope).hintCardDao(),
            FlashCardDatabase.Companion.getDatabase(context, scope).threeCardDao(),
            FlashCardDatabase.Companion.getDatabase(context, scope).multiChoiceCardDao()
        )
    }
    override val scienceSpecificRepository: ScienceSpecificRepository by lazy {
        OfflineScienceRepository(
            FlashCardDatabase.Companion.getDatabase(context, scope).notationCardDao()
        )
    }
    override val supabaseToRoomRepository: SupabaseToRoomRepository by lazy {
        OfflineSupabaseToRoomRepository(
            FlashCardDatabase.Companion.getDatabase(context, scope).supabaseDao()
        )
    }
    override val importRepository: ImportRepository by lazy {
        ImportRepositoryImpl(sharedSupabase)
    }
    override val sbTablesRepository: SBTablesRepository by lazy {
        SBTableRepositoryImpl(sharedSupabase)
    }
    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            sharedSupabase, syncedSupabase,
            FlashCardDatabase.Companion.getDatabase(context, scope).pwdDao()
        )
    }
    override val userExportedDecksRepository: UserExportedDecksRepository by lazy {
        UserExportDecksRepositoryImpl(sharedSupabase)
    }
    override val sharedSupabase: SupabaseClient by lazy { sharedSupabase }
    override val syncedSupabase: SupabaseClient by lazy { syncedSupabase }
}