<%-- 
    Document   : tour
    Created on : 09.10.2014, 0:37:32
    Author     : Helena.Grouk
--%>

<div id="main">
    <div class="left inner column">
        <ctg:RoleTag>
            <input type="checkbox" id="validTourStatus" name="status" value="1" class="boxMar" onchange="postTourDir('controller', 'ShowTour', 'POST')" />
                <fmt:message key="showValid" bundle="${ rb }" /><br>
            <input type="checkbox" id="invalidTourStatus" name="status" value="0" class="boxMar" onchange="postTourDir('controller', 'ShowTour', 'POST')" />
                <fmt:message key="showInvalid" bundle="${ rb }" /><br>
            <input type="checkbox" id="validTourDate" name="status" value="1" class="boxMar" onchange="postTourDir('controller', 'ShowTour', 'POST')" />
                <fmt:message key="showValidDate" bundle="${ rb }" /><br>
            <input type="checkbox" id="invalidTourDate" name="status" value="0" class="boxMar" onchange="postTourDir('controller', 'ShowTour', 'POST')" />
                <fmt:message key="showInvalidDate" bundle="${ rb }" /><br>
        </ctg:RoleTag>
        <script>
            boxChecking('validTourStatus', ${validTourStatus});
            boxChecking('invalidTourStatus', ${invalidTourStatus});
            boxChecking('validTourDate', ${validTourDate});
            boxChecking('invalidTourDate', ${invalidTourDate});
        </script>
                
        <select id="currTour" class="container" size="15" onclick="if(this.value)(post('controller', {selectId: this.value, command: 'showTour'}, 'POST'))">               
            <c:forEach items="${tourList}" var="row">
                <ctg:OptionTag status="${row.status}" date="${row.departDate}" 
                               valClass="menuHref" invalClass="grey menuHref" 
                               invalDateClass="blue menuHref" value="${row.idTour}">
                    ${row.departDate}
                </ctg:OptionTag>
            </c:forEach>
        </select>               
        <script>select("currTour", ${currTour.idTour});</script>         
        <ctg:RoleTag>
            <c:if test="${currDirection.status == 1}">
                <input class="large green awesome" type="submit" value="<fmt:message key="newTour" bundle="${ rb }" />" onclick="post('controller', {command: 'goCreateNewTour', currIdDirection: '${currDirection.idDirection}' }, 'POST')" />
            </c:if>
        </ctg:RoleTag>
    </div>
  
    <div class="full center inner column">
        <div class="parameterRow">
            <jsp:include page="/WEB-INF/other/statcalend.jsp" />
            <div class="mid input">
                <h1 class="labelH"><fmt:message key="date" bundle="${ rb }" />:</h1> 
            </div>
            <div class="input inner">
                <div class="inner">
                    <label class="inputLineContainer"><fmt:message key="from" bundle="${ rb }" /> ${departDate} <fmt:message key="to" bundle="${ rb }" /> ${arrivalDate}</label>
                </div>
            </div>
        </div>
        <div class="parameterRow">
            <div id="datepicker"></div>
        </div>
        
        <div class="parameterRow">
            <div class="mid input">
                <h1 class="labelH"><fmt:message key="price" bundle="${ rb }" />:</h1> 
            </div>
            <div class="input inner">
                <label class="inputLineContainer" name="price">${currTour.price}<fmt:message key="$" bundle="${ rb }" /></label>
            </div>      
        </div>      
            
        <div class="parameterRow">
            <div class="mid input">
                <h1 class="labelH"><fmt:message key="discount" bundle="${ rb }" />: </h1> 
            </div>
            <div class="input inner">
                <label name="discount" class="inputLineContainer">${currTour.discount}%</label>
            </div>      
        </div>      
            
        <div class="parameterRow">
            <div class="mid input">
                <h1 class="labelH"><fmt:message key="totalSeats" bundle="${ rb }" />: </h1> 
            </div>
            <div class="input inner">
                <label name="totalSeats" class="inputLineContainer">${currTour.totalSeats}</label>
            </div>      
        </div>             
            
        <div class="parameterRow">
            <div class="mid input">
                <h1 class="labelH"><fmt:message key="freeSeats" bundle="${ rb }" />: </h1> 
            </div>
            <div class="input inner">
                <label name="freeSeats" class="inputLineContainer">${currTour.freeSeats}</label>
            </div>      
        </div>   

        <ctg:RoleTag>
            <c:choose>
                <c:when test="${currTour.status == 1}">
                    <input class="large orange awesome" type="submit" value="<fmt:message key="editTour" bundle="${ rb }" />" onclick="post('controller', {command: 'goEditTour'}, 'POST')" />
                    <input class="large red awesome" type="submit" value="<fmt:message key="deleteTour" bundle="${ rb }" />" onclick="post('controller', {command: 'DeleteTour'}, 'POST')"/>
                    <ctg:ErrorMsgTag classErr="erNote" msg="${errorDelete}"><fmt:message key="${errorDelete}" bundle="${ rb }" /></ctg:ErrorMsgTag>
                    <ctg:ErrorMsgTag classErr="erNote" msg="${errorDeleteReason}"><fmt:message key="${errorDeleteReason}" bundle="${ rb }" /></ctg:ErrorMsgTag>
                </c:when>
                <c:when test="${currTour.status == 0}">
                    <input class="large green awesome" type="submit" value="<fmt:message key="restoreTour" bundle="${ rb }" />" onclick="post('controller', {command: 'RestoreTour'}, 'POST')"/>
                    <ctg:ErrorMsgTag classErr="erNote" msg="${errorRestore}"><fmt:message key="${errorRestore}" bundle="${ rb }" /></ctg:ErrorMsgTag>
                    <ctg:ErrorMsgTag classErr="erNote" msg="${errorRestoreReason}"><fmt:message key="${errorrestoreReason}" bundle="${ rb }" /></ctg:ErrorMsgTag>
                </c:when>
            </c:choose>   
            <input class="large blue awesome" type="submit" value="<fmt:message key="findOrders" bundle="${ rb }" />" onclick="post('controller', {command: 'goShowOrders', currIdTour: '${currTour.idTour}'}, 'POST')" />
            <input class="large yellow awesome" type="submit" value="<fmt:message key="formTouristList" bundle="${ rb }" />" onclick="post('controller', {command: 'ShowTourTourists', currIdTour: '${currTour.idTour}'}, 'POST')" />
        </ctg:RoleTag>
    </div>
</div>
