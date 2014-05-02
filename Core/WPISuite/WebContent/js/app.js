// Generated by CoffeeScript 1.7.1
(function() {
  var CardViewModel, EstimateViewModel, PlanningPokerViewModel, SessionViewModel,
    __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

  PlanningPokerViewModel = (function() {
    function PlanningPokerViewModel() {
      this.planningPokerSessions = ko.observableArray([]);
      this.requirements = [];
      this.team = [];
      this.activeSession = ko.observable();
      this.username = window.location.search.split('=')[1];
      $.ajax({
        dataType: 'json',
        url: 'API/requirementmanager/requirement',
        async: false,
        success: (function(_this) {
          return function(data) {
            var requirement, _i, _len;
            for (_i = 0, _len = data.length; _i < _len; _i++) {
              requirement = data[_i];
              requirement['voteValue'] = 0;
            }
            return _this.requirements = data;
          };
        })(this)
      });
      $.ajax({
        dataType: 'json',
        url: 'API/core/user',
        async: false,
        success: (function(_this) {
          return function(data) {
            return _this.team = data;
          };
        })(this)
      });
      $.ajax({
        dataType: 'json',
        url: 'API/planningpoker/planningpokersession',
        async: false,
        success: (function(_this) {
          return function(data) {
            var params, session, _i, _len, _results;
            _results = [];
            for (_i = 0, _len = data.length; _i < _len; _i++) {
              session = data[_i];
              params = {
                requirements: _this.requirements,
                team: _this.team,
                username: _this.username
              };
              _results.push(_this.planningPokerSessions.push(new SessionViewModel(session, params)));
            }
            return _results;
          };
        })(this)
      });
      this.openSessions = ko.computed((function(_this) {
        return function() {
          return _this.planningPokerSessions().filter(function(session) {
            return session.gameState() === 'OPEN';
          });
        };
      })(this));
      this.setActiveSession = (function(_this) {
        return function(session) {
          return _this.activeSession(session);
        };
      })(this);
    }

    return PlanningPokerViewModel;

  })();

  SessionViewModel = (function() {
    function SessionViewModel(data, params) {
      var estimate, field, requirement, userEstimate, value, voterList, _i, _j, _len, _len1, _ref, _ref1, _ref2;
      this.parent = parent;
      this.allRequirements = ko.observableArray(params.requirements);
      this.team = ko.observableArray(params.team);
      this.username = params.username;
      this.params = params;
      for (field in data) {
        value = data[field];
        this[field] = ko.observable(value);
      }
      this.params = params;
      this.params['usingDeck'] = this.isUsingDeck();
      this.params['deck'] = {};
      if (this.isUsingDeck()) {
        this.params['deck'] = this.sessionDeck();
      }
      this.requirementEstimates = ko.observableArray([]);
      _ref = this.allRequirements();
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        requirement = _ref[_i];
        if (_ref1 = requirement['id'], __indexOf.call(this.requirementIDs(), _ref1) >= 0) {
          userEstimate = {
            sessionID: this.uuid(),
            requirementID: requirement['id'],
            ownerName: this.username,
            vote: 0
          };
          voterList = [];
          _ref2 = this.estimates();
          for (_j = 0, _len1 = _ref2.length; _j < _len1; _j++) {
            estimate = _ref2[_j];
            if (estimate['requirementID'] === requirement['id']) {
              voterList.push(estimate['ownerName']);
              if (estimate['ownerName'] === this.username) {
                userEstimate['vote'] = estimate['vote'];
              }
            }
          }
          this.requirementEstimates.push(new EstimateViewModel(userEstimate, voterList, requirement, this.params));
        }
      }
      this.requirements = ko.computed((function(_this) {
        return function() {
          var result, _k, _len2, _ref3, _ref4;
          result = [];
          _ref3 = _this.allRequirements();
          for (_k = 0, _len2 = _ref3.length; _k < _len2; _k++) {
            requirement = _ref3[_k];
            if (_ref4 = requirement['id'], __indexOf.call(_this.requirementIDs(), _ref4) >= 0) {
              result.push(requirement);
            }
          }
          return result;
        };
      })(this));
    }

    return SessionViewModel;

  })();

  EstimateViewModel = (function() {
    function EstimateViewModel(userEstimate, voterList, req, params) {
      var cardValue, key, observableEstimate, value, _i, _len, _ref;
      this.requirement = ko.observable(req);
      this.requirements = ko.observable(params.requirements);
      this.team = ko.observable(params.team);
      this.usingDeck = ko.observable(params.usingDeck);
      this.deck = ko.observable(params.deck);
      this.username = params.username;
      this.voted = ko.observableArray(voterList);
      this.voteValue = ko.observable(0);
      observableEstimate = {};
      for (key in userEstimate) {
        value = userEstimate[key];
        observableEstimate[key] = ko.observable(value);
      }
      this.estimate = ko.observable(observableEstimate);
      if (this.usingDeck()) {
        this.cards = ko.observableArray([]);
        _ref = this.deck().numbersInDeck;
        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
          cardValue = _ref[_i];
          this.cards.push(new CardViewModel(cardValue, false, this));
        }
      }
      this.widthPercent = ko.computed((function(_this) {
        return function() {
          var numVotes, percent, teamSize;
          numVotes = _this.voted().length;
          teamSize = _this.team().length;
          percent = 0;
          if (teamSize > 0) {
            percent = parseInt((numVotes / teamSize) * 100);
          }
          return "" + percent + "%";
        };
      })(this));
      this.totalValue = ko.computed((function(_this) {
        return function() {
          var card, total, _j, _len1, _ref1;
          if (_this.usingDeck()) {
            total = 0;
            _ref1 = _this.cards();
            for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
              card = _ref1[_j];
              if (card.selected()) {
                total += card.value();
              }
            }
            return total;
          } else {
            return _this.voteValue();
          }
        };
      })(this));
      this.setVoteValue = (function(_this) {
        return function(value) {
          var card, deckNumbers, remaining, selectedCards, _j, _k, _len1, _len2, _ref1, _ref2, _results;
          if (_this.usingDeck()) {
            deckNumbers = _this.deck().numbersInDeck.slice(0);
            deckNumbers = deckNumbers.sort().reverse();
            selectedCards = [];
            remaining = value;
            for (_j = 0, _len1 = deckNumbers.length; _j < _len1; _j++) {
              cardValue = deckNumbers[_j];
              if (remaining >= cardValue) {
                selectedCards.push(cardValue);
                remaining -= cardValue;
              }
            }
            _ref1 = _this.cards();
            _results = [];
            for (_k = 0, _len2 = _ref1.length; _k < _len2; _k++) {
              card = _ref1[_k];
              if (_ref2 = card.value(), __indexOf.call(selectedCards, _ref2) >= 0) {
                _results.push(card.selected(true));
              } else {
                _results.push(void 0);
              }
            }
            return _results;
          } else {
            return _this.voteValue(value);
          }
        };
      })(this);
      this.setVoteValue(this.estimate().vote());
      this.notifySelection = (function(_this) {
        return function(cardViewModel) {
          var card, _j, _len1, _ref1;
          if (!_this.deck().allowMultipleSelections) {
            _ref1 = _this.cards();
            for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
              card = _ref1[_j];
              card.selected(false);
            }
            return cardViewModel.selected(true);
          }
        };
      })(this);
      this.submitVote = (function(_this) {
        return function() {
          _this.estimate().vote(parseInt(_this.totalValue()));
          return $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'API/Advanced/planningpoker/planningpokersession/update-estimate-website',
            data: ko.toJSON(_this.estimate()),
            success: function(data) {
              var _ref1;
              console.log('Vote successfully submitted');
              if (_ref1 = _this.username, __indexOf.call(_this.voted(), _ref1) < 0) {
                return _this.voted.push(_this.username);
              }
            },
            error: function() {
              return console.log('Error updating the estimate');
            }
          });
        };
      })(this);
    }

    return EstimateViewModel;

  })();

  CardViewModel = (function() {
    function CardViewModel(value, selected, parentViewModel) {
      this.value = ko.observable(value);
      this.selected = ko.observable(selected);
      this.parent = parentViewModel;
      this.cardClicked = (function(_this) {
        return function() {
          if (_this.selected()) {
            return _this.selected(false);
          } else {
            _this.selected(true);
            return _this.parent.notifySelection(_this);
          }
        };
      })(this);
    }

    return CardViewModel;

  })();

  $(function() {
    window.PokerVM = new PlanningPokerViewModel();
    return ko.applyBindings(window.PokerVM);
  });

}).call(this);
