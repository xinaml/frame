function logout() {
    var a = document.createElement('a')
    a.href = '/logout'
    a.target = '_blank'
    document.body.appendChild(a)
    a.click()
}