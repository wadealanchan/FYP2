const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database. 
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

var db = admin.firestore();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

const userCollection = "Users";

const postCollection = "posts";

exports.chatRequestFCM = functions.firestore
.document('/'+userCollection+'/{userId}/request/{requestId}')
.onWrite(event => {

    var targetUserId = event.params.userId;
    var requestId = event.params.requestId;
    var tmpId = requestId.split(":");
    var postId = tmpId[0];
    var aid = tmpId[1];
    var requestVal = event.data.get('request'); 
    
    var userRef = db.collection(userCollection).doc(aid);
    var postRef = db.collection(postCollection).doc(postId);
    return postRef.get().then(function(postData){
        var title = postData.data().title
        var category = postData.data().category
    return userRef.get().then(function(userData){
        var username = userData.data().name
        console.log("username: "+username);
    var topic = targetUserId;

    console.log("targetUserId: "+targetUserId);
    console.log("targetUserId: "+username);
    
    var payload = {
    data: {
        type: "chat_request"
    },
    notification:{
        title:"Tutor is here",
        body:username+" "+"is here to help you with your "+category+
        " question. Come and meet your teacher now! " ,
        sound: "default",
        click_action: "com.example.alan.fyp.action.ViewPagerMainpage"
    }
    };

    // Send a message to devices subscribed to the provided topic.
    return admin.messaging().sendToTopic(topic, payload)
    .then(function(response) {
        // See the MessagingTopicResponse reference documentation for the
        // contents of response.
        console.log("Successfully sent message:", response);
        return event.data;
    })
    .catch(function(error) {
        console.log("Error sending message:", error);
    });

}, function(){
})

}, function(){
})



});

exports.aggregateRatings = functions.firestore
  .document('/'+userCollection+'/{userId}/rating/{postId}')
  .onWrite(event => {
    // // Get value of the newly added rating
    var ratingVal = event.data.get('rating');

    // Get a reference to the restaurant
    var userRef = db.collection(userCollection).doc(event.params.userId);
    //db.collection('/'+userCollection+'/{userId}/request/xxxx:xxx').get().
             
    var newRating = event.data.get('numberOfStar');
    return userRef.get().then(function(userData){
      var avgRating = userData.data().avgRating || 0;
      console.log("avgRating:" + avgRating);

      return userRef.collection('rating').get().then(Snapshot => {
          var numOfRatings = Snapshot.size;
          console.log("numOfRatings" + numOfRatings);
          if (numOfRatings===1) {
              avgRating = newRating;
          } else {
            avgRating = (avgRating*numOfRatings+newRating)/(numOfRatings+1);
           console.log("avgRating" + avgRating);
          }
        
        return userRef.update({avgRating: avgRating});
      })/*.catch((err) => {
        console.log("error");
      });*/
    }, function(){

    })
    

    // Update aggregations in a transaction
    /*return db.transaction(transaction => {
      return transaction.get(restRef).then(restDoc => {
        // Compute new number of ratings
        var newNumRatings = restDoc.data('numRatings') + 1;

        // Compute new average rating
        var oldRatingTotal = restDoc.data('avgRating') * restDoc.data('numRatings');
        var newAvgRating = (oldRatingTotal + ratingVal) / newNumRatings;

        // Update restaurant info
        return transaction.update(restRef, {
          avgRating: newAvgRating,
          numRatings: newNumRatings
        });
      });
    });*/
});


exports.chatRequestFCM = functions.firestore
.document('/'+userCollection+'/{userId}/invitationRequest/{requestId}')
.onWrite(event => {

    var targetUserId = event.params.userId;
    var requestId = event.params.requestId;
    var tmpId = requestId.split(":");
    var postId = tmpId[0];
    var questionerId = tmpId[1];
    var requestVal = event.data.get('request'); 
    
    var userRef = db.collection(userCollection).doc(questionerId);
    var postRef = db.collection(postCollection).doc(postId);
    return postRef.get().then(function(postData){
        var title = postData.data().title
        var category = postData.data().category
        
    return userRef.get().then(function(userData){
        var username = userData.data().name
        console.log("username: "+username);
   
   
     var topic = targetUserId;

    
    
    var payload = {
    data: {
        type: "invitation_request"
    },
    notification:{
        title:"You are invited to help students here!",
        body:username+" "+"wants you to help his/her "+category+
        " question. Come and meet your student now! " ,
        sound: "default",
        click_action: "com.example.alan.fyp.action.ViewPagerMainpage"
    }
    };

    // Send a message to devices subscribed to the provided topic.
    return admin.messaging().sendToTopic(topic, payload)
    .then(function(response) {
        // See the MessagingTopicResponse reference documentation for the
        // contents of response.
        console.log("Successfully sent message:", response);
        return event.data;
    })
    .catch(function(error) {
        console.log("Error sending message:", error);
    });

}, function(){
})

}, function(){
})



});