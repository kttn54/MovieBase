package com.example.moviebase.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviebase.actor.ActorViewModel
import com.example.moviebase.getOrAwaitValueTest
import com.example.moviebase.model.DetailedActor
import com.example.moviebase.repositories.FakeActorRepository
import com.example.moviebase.ui.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ActorViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ActorViewModel
    private lateinit var fakeActorRepository: FakeActorRepository

    @Before
    fun setup() {
        fakeActorRepository = FakeActorRepository()
        viewModel = ActorViewModel(fakeActorRepository)
    }

    @Test
    fun `getDetailedActorInformation returns correct actor given id`() = runBlocking {
        val actor = DetailedActor(false, listOf("The Rock", "Rocky"), "An American and Canadian actor producer and semi-retired professional wrestler, signed with WWE", "1972-05-02", null, 2, "https://www.wwe.com/superstars/the-rock", 500, "nm0425005","Acting", "Dwayne Johnson", "Hayward, California, USA", 33.404, "/cgoy7t5Ve075naBPcewZrc08qGw.jpg")
        fakeActorRepository.addDetailedActor(actor)

        viewModel.getDetailedActorInformation(500)

        val detailedActor = viewModel.actorInformationLiveData.getOrAwaitValueTest()
        assertThat(detailedActor).isEqualTo(actor)
    }

}
