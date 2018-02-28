package demo

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import groovy.transform.CompileDynamic

class BookFavouriteController {

    static allowedMethods = [
            index: 'GET',
            favourite: 'POST',
    ]

    SpringSecurityService springSecurityService
    BookFavouriteDataService bookFavouriteDataService
    BookGormService bookGormService

    @Secured('permitAll')
    def index() {
        String username = loggedUsername()
        List<Long> bookIds = bookFavouriteDataService.findBookFavouriteBookId(username) //<1>
        List<BookImage> bookList = bookGormService.findAllByIds(bookIds) //<2>
        render view: '/book/index', model: [bookList: bookList]
    }

    @Secured('isAuthenticated()')
    def favourite(Long bookId) {
        String username = loggedUsername()
        bookFavouriteDataService.save(bookId, username) //<3>
        redirect(action: 'index')
    }

    @CompileDynamic
    protected String loggedUsername() {
        springSecurityService.principal.username
    }

}