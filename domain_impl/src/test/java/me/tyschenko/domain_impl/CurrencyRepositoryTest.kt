package me.tyschenko.domain_impl

import me.tyschenko.domain_api.CurrencyRepository
import me.tyschenko.network.ApiInterface
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal class CurrencyRepositoryTest {

    private val mockWebServer = MockWebServer()
    private lateinit var currencyRepository: CurrencyRepository

    @Before
    fun prepareRepositoryForApiInterface() {
        mockWebServer.start(8080)
        val apiInterface: ApiInterface = Retrofit.Builder()
                .baseUrl(mockWebServer.url("http://localhost:8080/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        currencyRepository = CurrencyRepositoryImpl(apiInterface)
    }

    @After
    fun closeSocket() {
        mockWebServer.close()
    }

    @Test
    fun testSuccessResponse() {
        mockWebServer.enqueue(generateMockedResponse("{\"base\":\"USD\",\"date\":\"2018-09-06\",\"rates\":{\"AUD\":1.3959}}"))
        currencyRepository.getCurrencyFromServer()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue {
                    it.ratesBaseToAmountMap["AUD"] == 1.3959
                }
    }

    @Test
    fun testInvalidBaseResponse() {
        mockWebServer.enqueue(generateMockedResponse("{\"error\":\"Invalid base\"}"))
        currencyRepository.getCurrencyFromServer()
                .test()
                .assertError {
                    it.message == "Invalid base"
                }
                .assertNotComplete()
    }

    @Test
    fun testErrorCodeResponse() {
        mockWebServer.enqueue(generateMockedErrorCodeResponse())
        currencyRepository.getCurrencyFromServer()
                .test()
                .assertError(Throwable::class.java)
                .assertNotComplete()
    }

    private fun generateMockedResponse(body: String): MockResponse {
        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody(body)
        return mockedResponse
    }

    private fun generateMockedErrorCodeResponse(): MockResponse {
        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(401)
        return mockedResponse
    }
}