"use strict"

angular
    .module('ngShortcut', [])

    .directive('shortcut', ['$document', ($document) ->
        link: (scope, element, attrs, controller) ->
            combos = attrs.shortcut.split(',')
            for combo in combos
                do (combo) ->
                    parts = combo.split('-')
                    keycode = parseInt(parts[parts.length-1], 10)
                    modifiers = parts.slice(0, parts.length-1)

                    handler = (e) ->
                        return if e.keyCode != keycode or ('closest' of element and element.closest('.ng-leave').length)
                        e.stopImmediatePropagation()

                        for meta in ['shift', 'ctrl', 'alt', 'meta']
                            return if !(modifiers.indexOf(meta) == -1) != e["#{meta}Key"]
                            
                        e.preventDefault() if scope.$eval(attrs.shortcutPreventDefault)

                        eventName = attrs.shortcutEvent or 'click'
                        if 'shortcutTriggerHandler' of attrs
                            element.triggerHandler(eventName)
                        else
                            element.trigger(eventName)

                    $document.on 'keydown', handler
                    element.on '$destroy', ->
                        $document.off('keydown', handler)
    ])
