'use strict';

angular.module('metadatamanagementApp').filter('displayPersons',
  function() {
    var numberOfPeopleToDisplay = 20;
    var personToString = function(person) {
      if (person.middleName) {
        return person.lastName + ', ' +
          person.firstName.charAt(0).toUpperCase() + '.' +
          ' ' + person.middleName.charAt(0).toUpperCase() + '.';
      } else {
        return person.lastName + ', ' +
          person.firstName.charAt(0).toUpperCase() + '.';
      }
    };
    return function(persons) {
      if (!persons || !persons.length) {
        return '';
      }
      if (persons.length === 1) {
        return personToString(persons[0]);
      } else if (2 <= persons.length &&
          persons.length <= numberOfPeopleToDisplay) {
        return persons.map(personToString).reduce(
          function(accumulator, current, index) {
            if (persons.length - 1 === index) {
              return accumulator + ' & ' + current;
            }
            return accumulator + ', ' + current;
          });
      } else if (persons.length > numberOfPeopleToDisplay) {
        return persons.map(personToString).reduce(
          function(accumulator, current, index) {
            // this is the last person to display
            if (index === numberOfPeopleToDisplay - 1) {
              return accumulator + ', ... ';
            }
            // skip all people but display the last
            if (numberOfPeopleToDisplay - 1  < index &&
              index < persons.length - 1) {
              return accumulator;
            }
            // display the last person
            if (persons.length - 1 === index) {
              return accumulator + '& ' + current;
            }
            return accumulator + ', ' + current;
          });
      }
    };
  });
